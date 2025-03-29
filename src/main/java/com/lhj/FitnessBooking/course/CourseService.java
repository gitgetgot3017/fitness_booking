package com.lhj.FitnessBooking.course;

import com.lhj.FitnessBooking.course.exception.*;
import com.lhj.FitnessBooking.domain.Course;
import com.lhj.FitnessBooking.domain.History;
import com.lhj.FitnessBooking.domain.Member;
import com.lhj.FitnessBooking.domain.Reservation;
import com.lhj.FitnessBooking.dto.*;
import com.lhj.FitnessBooking.history.HistoryRepository;
import com.lhj.FitnessBooking.reservation.ReservationRepository;
import com.lhj.FitnessBooking.reservation.exception.ReservationFailException;
import com.lhj.FitnessBooking.response.CourseMainResponse;
import com.lhj.FitnessBooking.subscription.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.lhj.FitnessBooking.domain.CourseStatus.CANCELED;
import static com.lhj.FitnessBooking.domain.CourseStatus.RESERVED;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final SubscriptionRepository subscriptionRepository;
    private final HistoryRepository historyRepository;
    private final CourseRepository courseRepository;
    private final ReservationRepository reservationRepository;
    private final SmsService smsService;

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 수강권 만료의 이유로 수강권이 존재하지 않는 회원: 이용 내역만 보여준다. try 일부 -> finally
     * 수강권이 존재하는 회원: try 전체 -> finally
     */
    public CourseMainResponse showCourseMain(Member member, LocalDate date) {

        CourseMainHeader courseMainHeader = null;
        List<CourseInfoTmp> courses = null;
        try {
            courseMainHeader = subscriptionRepository.getSubscription(member, LocalDate.now());

            // 오늘의 수업을 조회하면 현재 시각 이후의 수업만 보여주고, 나머지 일자의 수업을 조회하면 전체 수업을 보여준다.
            LocalTime startTime = LocalTime.of(0, 0, 0);
            if (date.isEqual(LocalDate.now())) {
                startTime = LocalTime.now();
            }
            courses = courseRepository.getCourses(date, startTime); // 특정 날짜(date)의 수업 조회
        } finally {
            LocalDate today = LocalDate.now();
            List<History> history = historyRepository.getHistoryDate(member, today.getYear(), today.getMonthValue());
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
        validateCourseAvailability(errorResponse, date, courseId, member, leftCount);
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
                courseInfoTmp.getInstructorImgUrl(),
                courseInfoTmp.getCourseName(),
                courseInfoTmp.getCourseStartTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                courseInfoTmp.getCourseEndTime().format(DateTimeFormatter.ofPattern("HH:mm")),
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
     * 수강 대기하기 위한 조건
     * 5. 해당 수업의 예약 여석이 존재해야 한다.
     */
    private void validateCourseAvailability(Map<String, String> errorResponse, LocalDate date, Long courseId, Member member, int leftCount) {

        List<History> enrolledDates = historyRepository.getReservedAndEnrolled(date);
        if (enrolledDates.size() >= 2) {
            errorResponse.put("enrollmentLimitExceeded", "하루에 수강 가능한 최대 횟수는 2회입니다.");
        }

        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotExistCourseException("존재하지 않는 수업입니다."));
        if (historyRepository.checkAlreadyRegistered(date, course).isPresent()) {
            errorResponse.put("duplicateEnrollment", "이미 신청한 수업입니다.");
        }

        if (leftCount == 0) {
            errorResponse.put("courseExpiration", "수강 횟수가 남아있지 않습니다.");
        }

        String courseCountKey = "course:" + date + ":" + courseId + ":count";
        Integer courseCount = (Integer) redisTemplate.opsForValue().get(courseCountKey);
        if (courseCount == null) { // redis에서 조회 후 데이터가 없을 시 DB 조회
            courseCount = courseRepository.getCourseCount(date, courseId);
        }
        if (courseCount >= 6) {
            errorResponse.put("classCapacityExceeded", "수강 정원을 초과하였습니다.");
        }

        String courseWaitingKey = "course:" + date + ":" + courseId + ":waiting";
        Long waitingSize = redisTemplate.opsForList().size(courseWaitingKey);
        if (waitingSize == null) { // redis에서 조회 후 데이터가 없을 시 DB 조회
            List<Reservation> reservations = reservationRepository.findByCourseDateAndCourse(date, course);
            waitingSize = Long.valueOf(reservations.size());
        }
        if (waitingSize >= 6) {
            errorResponse.put("reservationCapacityExceeded", "대기 정원을 초과하였습니다.");
        }

        Optional<Reservation> ifReserve = reservationRepository.findByCourseDateAndCourseAndMember(date, course, member);
        if (ifReserve.isPresent()) {
            errorResponse.put("alreadyWaitCourse", "이미 대기 신청을 하였습니다.");
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
        if (historyRepository.ifAfter4hour(member, date, course, LocalTime.now().plusHours(4)).isPresent()) {
            errorResponse.put("exceeded4HourLimit", "수업 시작 4시간 전까지만 취소 가능합니다.");
        }
    }

    /**
     * 수강 예약을 하려면?
     * 1. 수강 정원 1명 증가시키기
     * 2. history에 RESERVED 추가
     * 3. subscription의 reservedCount + 1
     * 4. 해당 강좌의 인기 점수 1만큼 증가
     */
    public void reserveCourse(Member member, LocalDate date, Long courseId) {

        validateCourseReservationPossibility(member, date, courseId); // 추천 수업 예약을 통해 '예약하기' 버튼을 접하는 경우를 위함

        // 1.
        String courseCountKey = "course:" + date + ":" + courseId + ":count";
        Integer courseCount = (Integer) redisTemplate.opsForValue().get(courseCountKey);

        if (courseCount == null) { // redis에 키가 존재하지 않으면 DB 조회 후 저장
            courseCount = courseRepository.getCourseCountWithLock(date, courseId);
            redisTemplate.opsForValue().set(courseCountKey, courseCount);
        }

        if (courseCount >= 6) {
            throw new ReservationFailException("수강 인원 초과로 예약에 실패하셨습니다.");
        }

        redisTemplate.opsForValue().increment(courseCountKey);

        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotExistCourseException("존재하지 않는 수업입니다."));
        courseRepository.increaseCourseCount(date, course);

        // 2.
        History history = new History(member, date, course, date.getYear(), date.getMonthValue(), LocalDateTime.now(), RESERVED);
        historyRepository.save(history);

        // 3.
        subscriptionRepository.increaseReservedCount(member);

        // 4.
        String popularClassesKey = "class:popular";
        String classValue = "class" + date + ":" + courseId;
        redisTemplate.opsForZSet().incrementScore(popularClassesKey, classValue, 1);
    }

    private void validateCourseReservationPossibility(Member member, LocalDate date, Long courseId) {

        List<History> enrolledDates = historyRepository.getReservedAndEnrolled(date);
        if (enrolledDates.size() >= 2) {
            throw new EnrollmentLimitExceededException("하루에 수강 가능한 최대 횟수는 2회입니다.");
        }

        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotExistCourseException("존재하지 않는 수업입니다."));
        if (historyRepository.checkAlreadyRegistered(date, course).isPresent()) {
            throw new DuplicateEnrollmentException("이미 신청한 수업입니다.");
        }

        CourseMainHeader subscription = subscriptionRepository.getSubscription(member, LocalDate.now());
        if (subscription.getAvailableCount() - subscription.getCompletedCount() - subscription.getReservedCount() == 0) {
            throw new CourseExpirationException("수강 횟수가 남아있지 않습니다.");
        }
    }

    /**
     * 수강 대기를 하려면?
     * 1. Reservation에 레코드 추가
     * 2. Redis의 List에 대기자 추가
     * 3. 해당 강좌의 인기 점수 1만큼 증가
     */
    public void waitCourse(Member member, LocalDate date, Long courseId) {

        // 1.
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotExistCourseException("해당 수업은 존재하지 않습니다."));
        Reservation reservation = new Reservation(date, course, member , LocalDateTime.now());
        reservationRepository.save(reservation);

        // 2.
        String courseWaitingKey = "course:" + date + ":" + courseId + ":waiting";
        redisTemplate.opsForList().rightPush(courseWaitingKey, member.getId());

        // 3.
        String popularClassesKey = "class:popular";
        String classValue = "class" + date + ":" + courseId;
        redisTemplate.opsForZSet().incrementScore(popularClassesKey, classValue, 1);
    }

    /**
     * 수강 취소를 하려면?
     * 1. 수강 정원 1명 감소시키기
     * 2. hisotry에 CANCELED 추가
     * 3. subscription의 reservedCount - 1
     * 4. 대기자가 존재할 시, (1) 대기자 전원에게 문자 메시지 발송 (2) reservation에서 대기자 전원 제거
     * 5. 해당 강좌의 인기 점수 1만큼 감소
     */
    public void cancelCourse(Member member, LocalDate date, Long courseId) {

        // 1.
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotExistCourseException("존재하지 않는 수업입니다."));
        courseRepository.decreaseCourseCount(date, course);

        // 2.
        History history = new History(member, date, course, date.getYear(), date.getMonthValue(), LocalDateTime.now(), CANCELED);
        historyRepository.save(history);

        // 3.
        subscriptionRepository.decreaseReservedCount(member);

        // 4.
        List<Reservation> reservations = reservationRepository.findByCourseDateAndCourse(date, course);
        if (reservations.size() == 0) {
            return;
        }
        smsService.sendSms(member.getPhone(), date, courseId);
        reservationRepository.deleteReservations(date, course);

        // 5.
        String popularClassesKey = "class:popular";
        String classValue = "class" + date + ":" + courseId;
        redisTemplate.opsForZSet().incrementScore(popularClassesKey, classValue, -1);
    }

    /**
     * 대기 취소를 하려면?
     * 1. Reservation에서 레코드 제거
     * 2. 해당 강좌의 인기 점수 1만큼 감소
     */
    public void cancelWaiting(LocalDate date, Long courseId, Member member) {

        // 1.
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotExistCourseException("존재하지 않는 수업입니다."));
        reservationRepository.deleteReservation(date, course, member);

        // 2.
        String popularClassesKey = "class:popular";
        String classValue = "class" + date + ":" + courseId;
        redisTemplate.opsForZSet().incrementScore(popularClassesKey, classValue, 1);
    }

    public List<CourseHistoryDto> showCourseHistory(Member member, LocalDate date) {

        List<CourseHistoryTmp> courseHistoryTmpList = historyRepository.getHistory(member, date.getYear(), date.getMonthValue());
        return changeAllCourseHistoryTmpToCourseHistoryDto(courseHistoryTmpList);
    }

    private List<CourseHistoryDto> changeAllCourseHistoryTmpToCourseHistoryDto(List<CourseHistoryTmp> courseHistoryTmpList) {

        List<CourseHistoryDto> courseHistoryDtoList = new ArrayList<>();
        for (CourseHistoryTmp courseHistoryTmp : courseHistoryTmpList) {
            courseHistoryDtoList.add(new CourseHistoryDto(
                    courseHistoryTmp.getCourseDate(),
                    courseHistoryTmp.getInstructorName(),
                    courseHistoryTmp.getCourseName(),
                    courseHistoryTmp.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                    courseHistoryTmp.getStartTime().plusMinutes(50).format(DateTimeFormatter.ofPattern("HH:mm")),
                    courseHistoryTmp.getAttendeeCount()
            ));
        }
        return courseHistoryDtoList;
    }

    public Set<Object> getPopularTop3Classes() {
        
        return redisTemplate.opsForZSet().reverseRange("class:popular", 0, 2);
    }
}
