package com.lhj.FitnessBooking.course;

import com.lhj.FitnessBooking.course.exception.CannotAccessException;
import com.lhj.FitnessBooking.course.exception.NotExistCourseException;
import com.lhj.FitnessBooking.courseHistory.CourseHistoryRepository;
import com.lhj.FitnessBooking.domain.Course;
import com.lhj.FitnessBooking.domain.History;
import com.lhj.FitnessBooking.domain.Member;
import com.lhj.FitnessBooking.dto.*;
import com.lhj.FitnessBooking.history.HistoryRepository;
import com.lhj.FitnessBooking.reservation.ReservationRepository;
import com.lhj.FitnessBooking.response.CourseMainResponse;
import com.lhj.FitnessBooking.subscription.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                .orElseThrow(() -> new NotExistCourseException("해당 날짜에는 해당 수업이 존재하지 않습니다"));
        List<History> cancelCount = historyRepository.getCancelCount(member, date);

        return changeIntoCourseMainResponse(courseMainHeader, date, courseInfoTmp, cancelCount);
    }

    private CourseDetailResponse changeIntoCourseMainResponse(CourseMainHeader courseMainHeader, LocalDate date, CourseInfoTmp courseInfoTmp, List<History> cancelCount) {

        return new CourseDetailResponse(
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
}
