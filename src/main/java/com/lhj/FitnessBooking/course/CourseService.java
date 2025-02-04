package com.lhj.FitnessBooking.course;

import com.lhj.FitnessBooking.course.exception.CannotAccessException;
import com.lhj.FitnessBooking.course.exception.ClassCapacityExceededException;
import com.lhj.FitnessBooking.course.exception.NotExistCourseException;
import com.lhj.FitnessBooking.courseHistory.CourseHistoryRepository;
import com.lhj.FitnessBooking.domain.*;
import com.lhj.FitnessBooking.dto.*;
import com.lhj.FitnessBooking.history.HistoryRepository;
import com.lhj.FitnessBooking.reservation.ReservationRepository;
import com.lhj.FitnessBooking.response.CourseMainResponse;
import com.lhj.FitnessBooking.subscription.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.lhj.FitnessBooking.domain.CourseStatus.ENROLLED;
import static com.lhj.FitnessBooking.domain.CourseStatus.RESERVED;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final SubscriptionRepository subscriptionRepository;
    private final HistoryRepository historyRepository;
    private final CourseRepository courseRepository;
    private final CourseHistoryRepository courseHistoryRepository;
    private final ReservationRepository reservationRepository;

    /**
     * 수강권 만료의 이유로 수강권이 존재하지 않는 회원: 이용 내역만 보여준다. try 일부 -> finally
     * 수강권이 존재하는 회원: try 전체 -> finally
     */
    public CourseMainResponse showCourseMain(Member member, LocalDate date) {

        LocalDate today = LocalDate.now();

        CourseMainHeader courseMainHeader = null;
        List<CourseInfoTmp> courses = null;
        try {
            courseMainHeader = subscriptionRepository.getSubscription(member, LocalDate.now());

            if (date.isEqual(today)) { // 오늘의 수업 조회
                courses = courseRepository.getTodayCourses(today, LocalTime.now());
            } else if (date.isEqual(today.plusDays(1))) { // 내일의 수업 조회
                courses = courseRepository.getTomorrowCourses(today);
            }
        } finally {
            List<History> history = historyRepository.getHistory(member, today.getYear(), today.getMonthValue(), ENROLLED, RESERVED);
            return changeIntoCourseMainResponse(courseMainHeader, history, courses);
        }
    }

    private CourseMainResponse changeIntoCourseMainResponse(CourseMainHeader courseMainHeader, List<History> historyList, List<CourseInfoTmp> courseInfoTmpList) {

        if (courseMainHeader == null) {
            return new CourseMainResponse(
                    null,
                    null,
                    null,
                    null,
                    null,
                    0,
                    0,
                    0,
                    changeHistoryToDate(historyList),
                    changeAllCourseInfoTmpToCourseInfo(courseInfoTmpList));
        }

        return new CourseMainResponse(
                courseMainHeader.getMemberName(),
                courseMainHeader.getMemberNum(),
                courseMainHeader.getSubscriptionName(),
                courseMainHeader.getStartDate(),
                courseMainHeader.getEndDate(),
                courseMainHeader.getCompletedCount(),
                courseMainHeader.getAvailableCount(),
                courseMainHeader.getReservedCount(),
                changeHistoryToDate(historyList),
                changeAllCourseInfoTmpToCourseInfo(courseInfoTmpList));
    }

    private Set<Integer> changeHistoryToDate(List<History> historyList) {

        Set<Integer> dates = new HashSet<>();
        for (History history : historyList) {
            dates.add(history.getRegDateTime().getDayOfMonth());
        }
        return dates;
    }

    /**
     * 수강권 만료의 이유로 수강권이 존재하지 않는 회원: 홈>그룹예약>예약상세 페이지 접근 불가
     */
    public CourseDetailResponse showCourseDetail(Member member, LocalDate date, Long courseId) {

        CourseMainHeader courseMainHeader;
        try {
            courseMainHeader = subscriptionRepository.getSubscription(member, LocalDate.now());
        } catch (NullPointerException e) {
            throw new CannotAccessException("비회원은 접근할 수 없습니다");
        }

        CourseInfoTmp courseInfoTmp = courseRepository.getCourseDetailCourseInfo(date, courseId)
                .orElseThrow(() -> new NotExistCourseException("해당 날짜에는 해당 수업이 존재하지 않습니다."));
        List<History> cancelCount = historyRepository.getCancelCount(member, date);

        CourseDetailInfo courseDetailInfo = changeIntoCourseMainResponse(courseMainHeader, date, courseInfoTmp, cancelCount);

        Map<String, String> errorResponse = new HashMap<>();
        int leftCount = courseMainHeader.getAvailableCount() - courseMainHeader.getCompletedCount() - courseMainHeader.getReservedCount(); // 남은 수강 횟수
        validateCourseAvailability(errorResponse, date, courseId, leftCount);
        validateCourseCancelAvailability(errorResponse, member, date, courseId);

        return makeCourseDetailInfo(courseDetailInfo, errorResponse);
    }

    private CourseDetailResponse makeCourseDetailInfo(CourseDetailInfo courseDetailInfo, Map<String, String> errorResponse) {

        if (errorResponse.size() == 0) {
            return new CourseDetailResponse(true, courseDetailInfo, errorResponse);
        }
        return new CourseDetailResponse(false, courseDetailInfo, errorResponse);
    }

    private CourseDetailInfo changeIntoCourseMainResponse(CourseMainHeader courseMainHeader, LocalDate date, CourseInfoTmp courseInfoTmp, List<History> cancelCount) {

        return new CourseDetailInfo(
                courseMainHeader.getMemberName(),
                courseMainHeader.getMemberNum(),
                courseMainHeader.getSubscriptionName(),
                courseMainHeader.getStartDate(),
                courseMainHeader.getEndDate(),
                courseMainHeader.getCompletedCount(),
                courseMainHeader.getAvailableCount(),
                courseMainHeader.getReservedCount(),
                date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                changeCourseInfoTmpToCourseInfo(courseInfoTmp),
                3 - cancelCount.size()
        );
    }

    private List<CourseInfo> changeAllCourseInfoTmpToCourseInfo(List<CourseInfoTmp> courseInfoTmpList) {

        if (courseInfoTmpList == null) {
            return new ArrayList<>();
        }

        List<CourseInfo> courseInfoList = new ArrayList<>();
        for (CourseInfoTmp courseInfoTmp : courseInfoTmpList) {
            courseInfoList.add(changeCourseInfoTmpToCourseInfo(courseInfoTmp));
        }
        return courseInfoList;
    }

    private CourseInfo changeCourseInfoTmpToCourseInfo(CourseInfoTmp courseInfoTmp) {
        return new CourseInfo(
                courseInfoTmp.getCourseId(),
                courseInfoTmp.getInstructorName(),
                courseInfoTmp.getCourseName(),
                courseInfoTmp.getCourseStartTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                courseInfoTmp.getCourseStartTime().plusMinutes(50).format(DateTimeFormatter.ofPattern("HH:mm")),
                courseInfoTmp.getAttendeeCount()
        );
    }

    /**
     * 수강 신청하기 위한 조건
     * 1. 하루에 수업을 신청한 횟수가 1번 이하여야 한다.
     * 2. 이미 수강신청한 수업이 아니여야 한다.
     * 3. 수강 횟수가 남아 있어야 한다.
     * 4. 해당 수업의 여석이 존재해야 한다.
     *
     * 수강 예약하기 위한 조건
     * 5. 해당 수업의 예약 여석이 존재해야 한다.
     */
    private void validateCourseAvailability(Map<String, String> errorResponse, LocalDate date, Long courseId, int leftCount) {

        List<CourseHistory> enrolledDates = courseHistoryRepository.findByDate(date);
        if (enrolledDates.size() >= 2) {
            errorResponse.put("enrollmentLimitExceeded", "하루에 수강 가능한 최대 횟수는 2회입니다.");
        }

        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotExistCourseException("존재하지 않는 수업입니다."));
        if (courseHistoryRepository.findByDateAndCourse(date, course).isEmpty()) {
            errorResponse.put("duplicateEnrollment", "이미 신청한 수업입니다.");
        }

        if (leftCount == 0) {
            errorResponse.put("courseExpiration", "수강 횟수가 남아있지 않습니다.");
        }

        int courseCount = courseRepository.getCourseCount(date, courseId);
        if (courseCount >= 6) {
            errorResponse.put("classCapacityExceeded", "수강 정원을 초과하였습니다.");
        }

        List<Reservation> reservations = reservationRepository.findByCourseDateAndCourse(date, course);
        if (reservations.size() >= 6) {
            errorResponse.put("reservationCapacityExceeded", "예약 정원을 초과하였습니다.");
        }
    }

    /**
     * 수강 취소하기 위한 조건
     * 1. 하루에 수업을 취소한 횟수가 2번 이하여야 한다.
     * 2. 신청한 수업에 대해, 수업 시작 시간 4시간 전이어야 한다.
     */
    private void validateCourseCancelAvailability(Map<String, String> errorResponse, Member member, LocalDate date, Long courseId) {

        List<History> cancelHistory = historyRepository.getCancelCount(member, date);
        if (cancelHistory.size() >= 3) {
            errorResponse.put("cancellationLimitExceeded", "수강 취소는 하루에 최대 3번까지만 가능합니다.");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotExistCourseException("해당 수업은 존재하지 않습니다."));
        if (historyRepository.ifBefore4hour(member, date, course, LocalTime.now()).isEmpty()) {
            errorResponse.put("exceeded4HourLimit", "수업 시작 4시간 전까지만 취소 가능합니다.");
        }
    }

    public void reserveCourse(Member member, LocalDate date, Long courseId) {

        int courseCount = courseRepository.getCourseCountWithLock(date, courseId);
        if (courseCount >= 6) {
            return;
        }

        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotExistCourseException("존재하지 않는 수업입니다."));
        courseRepository.increaseCourseCount(date, course);

        historyRepository.save(new History(member, course, date.getYear(), date.getMonthValue(), LocalDateTime.now(), RESERVED));
        subscriptionRepository.increaseReservedCount(member);
    }

    public void waitCourse(Member member, LocalDate date, Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotExistCourseException("해당 수업은 존재하지 않습니다."));

        Reservation reservation = new Reservation(date, course, member , LocalDateTime.now());
        reservationRepository.save(reservation);
    }
}
