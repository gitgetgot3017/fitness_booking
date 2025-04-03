package com.lhj.fitnessbooking.course;

import com.lhj.fitnessbooking.domain.course.domain.Course;
import com.lhj.fitnessbooking.domain.course.domain.DayOfWeek;
import com.lhj.fitnessbooking.domain.course.dto.CourseHistoryDto;
import com.lhj.fitnessbooking.domain.course.dto.CourseMainHeader;
import com.lhj.fitnessbooking.domain.course.repository.CourseRepository;
import com.lhj.fitnessbooking.domain.course.service.CourseService;
import com.lhj.fitnessbooking.domain.coursehistory.domain.CourseHistory;
import com.lhj.fitnessbooking.domain.coursehistory.repository.CourseHistoryRepository;
import com.lhj.fitnessbooking.domain.history.domain.History;
import com.lhj.fitnessbooking.domain.history.repository.HistoryRepository;
import com.lhj.fitnessbooking.domain.instructor.domain.Instructor;
import com.lhj.fitnessbooking.domain.instructor.repository.InstructorRepository;
import com.lhj.fitnessbooking.domain.member.domain.Member;
import com.lhj.fitnessbooking.domain.member.repository.MemberRepository;
import com.lhj.fitnessbooking.domain.reservation.domain.Reservation;
import com.lhj.fitnessbooking.domain.reservation.exception.ReservationFailException;
import com.lhj.fitnessbooking.domain.reservation.repository.ReservationRepository;
import com.lhj.fitnessbooking.domain.subscription.domain.Subscription;
import com.lhj.fitnessbooking.domain.subscription.repository.SubscriptionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.lhj.fitnessbooking.domain.course.domain.DayOfWeek.TUES;
import static com.lhj.fitnessbooking.domain.history.domain.CourseStatus.*;
import static com.lhj.fitnessbooking.domain.member.domain.MemberGrade.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class CourseServiceTest {

    @Autowired MemberRepository memberRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired InstructorRepository instructorRepository;
    @Autowired CourseHistoryRepository courseHistoryRepository;
    @Autowired HistoryRepository historyRepository;
    @Autowired SubscriptionRepository subscriptionRepository;
    @Autowired ReservationRepository reservationRepository;

    @Autowired
    CourseService courseService;

    @DisplayName("수강 예약하기: 예약에 성공하는 경우")
    @Test
    void reserveCourseSuccess() {

        // given
        Course course = saveCourse("캐딜락", TUES, LocalTime.of(18, 0), LocalTime.of(0, 0));
        CourseHistory courseHistory = saveCourseHistory(course, LocalDate.of(2025, 2, 4), 5);

        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));
        LocalDate courseDate = LocalDate.of(2025, 2, 4);
        Long courseId = course.getId();

        saveSubscription(member, LocalDate.of(2024, 6, 18), LocalDate.of(2025, 2, 23), 2, 73, 77);
        CourseMainHeader subscription = subscriptionRepository.getSubscription(member, courseDate);
        int reservedCount = subscription.getReservedCount();

        // when
        courseService.reserveCourse(member, courseDate, courseId);

        // then
        int courseCount = courseRepository.getCourseCount(courseDate, courseId);
        assertThat(courseCount).isEqualTo(courseHistory.getCount() + 1);

        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(1);
        assertThat(historyList.get(0).getStatus()).isSameAs(RESERVED);

        assertThat(subscriptionRepository.getSubscription(member, courseDate).getReservedCount()).isEqualTo(reservedCount + 1);
    }

    @DisplayName("수강 예약하기: 정원 초과로 예약에 실패하는 경우")
    @Test
    void reserveCourseFail() {

        Course course = saveCourse("캐딜락", TUES, LocalTime.of(18, 0), LocalTime.of(0, 0));
        saveCourseHistory(course, LocalDate.of(2025, 2, 4), 6);

        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));
        LocalDate courseDate = LocalDate.of(2025, 2, 4);
        Long courseId = course.getId();

        // when, then
        assertThatThrownBy(() -> courseService.reserveCourse(member, courseDate, courseId))
                .isInstanceOf(ReservationFailException.class);
    }

    @DisplayName("수강 대기하기")
    @Test
    void waitCourse() {

        // given
        Course course = saveCourse("캐딜락", TUES, LocalTime.of(18, 0), LocalTime.of(0, 0));

        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));
        LocalDate courseDate = LocalDate.of(2025, 2, 5);
        Long courseId = course.getId();

        // when
        courseService.waitCourse(member, courseDate, courseId);

        // then
        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).hasSize(1);
        assertThat(reservations.get(0).getCourse().getId()).isEqualTo(courseId);
        assertThat(reservations.get(0).getCourseDate()).isEqualTo(courseDate);
    }

    @DisplayName("수강 취소하기")
    @Test
    void cancelCourse() {

        // given
        Course course = saveCourse("캐딜락", TUES, LocalTime.of(18, 0), LocalTime.of(0, 0));
        CourseHistory courseHistory = saveCourseHistory(course, LocalDate.of(2025, 2, 5), 6);

        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));
        LocalDate courseDate = LocalDate.of(2025, 2, 5);
        Long courseId = course.getId();

        saveSubscription(member, LocalDate.of(2024, 6, 18), LocalDate.of(2025, 2, 23), 2, 73, 77);
        CourseMainHeader subscription = subscriptionRepository.getSubscription(member, courseDate);
        int reservedCount = subscription.getReservedCount();

        // when
        courseService.cancelCourse(member, courseDate, courseId);

        // then
        int courseCount = courseRepository.getCourseCount(courseDate, courseId);
        assertThat(courseCount).isEqualTo(courseHistory.getCount() - 1);

        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(1);
        assertThat(historyList.get(0).getStatus()).isSameAs(CANCELED);

        assertThat(subscriptionRepository.getSubscription(member, courseDate).getReservedCount()).isEqualTo(reservedCount - 1);

        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).hasSize(0);
    }

    @DisplayName("수강 대기 취소하기")
    @Test
    void cancelWaiting() {

        // given
        Course course = saveCourse("캐딜락", TUES, LocalTime.of(18, 0), LocalTime.of(0, 0));

        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));
        LocalDate courseDate = LocalDate.of(2025, 2, 5);
        Long courseId = course.getId();

        Reservation reservation = new Reservation(courseDate, course, member, LocalDateTime.of(2025, 2, 5, 17, 15));
        reservationRepository.save(reservation);

        // when
        courseService.cancelWaiting(courseDate, courseId, member);

        // then
        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).hasSize(0);
    }

    @DisplayName("수강 기록 요청하기")
    @Test
    void showCourseHistory() {

        // given
        Course course1 = saveCourse("캐딜락", TUES, LocalTime.of(18, 0), LocalTime.of(0, 0));
        Course course2 = saveCourse("캐딜락", TUES, LocalTime.of(18, 0), LocalTime.of(0, 0));
        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));
        saveCourseHistory(course1, LocalDate.of(2025, 2, 5), 5);
        saveCourseHistory(course2, LocalDate.of(2025, 2, 5), 4);

        historyRepository.save(new History(member, LocalDate.of(2025, 2, 5), course1, 2025, 2, LocalDateTime.of(2025, 2, 5, 23, 34), RESERVED));
        historyRepository.save(new History(member, LocalDate.of(2025, 2, 5), course1, 2025, 2, LocalDateTime.of(2025, 2, 5, 23, 34), CANCELED));
        historyRepository.save(new History(member, LocalDate.of(2025, 2, 5), course2, 2025, 2, LocalDateTime.of(2025, 2, 5, 23, 34), CANCELED));
        historyRepository.save(new History(member, LocalDate.of(2025, 2, 5), course2, 2025, 2, LocalDateTime.of(2025, 2, 5, 23, 34), RESERVED));
        historyRepository.save(new History(member, LocalDate.of(2025, 2, 5), course2, 2025, 2, LocalDateTime.of(2025, 2, 5, 23, 34), ENROLLED));

        // when
        List<CourseHistoryDto> historyList = courseService.showCourseHistory(member, LocalDate.of(2025, 2, 5));

        // then
        assertThat(historyList).hasSize(2);
    }

    private Member saveMember(String memberNum, String password, String name, String phone, boolean gender, LocalDate regDate) {

        Member member = new Member(memberNum, password, name, phone, gender, MEMBER, regDate);
        memberRepository.save(member);
        return member;
    }

    private Course saveCourse(String name, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {

        Instructor instructor = new Instructor(name);
        instructorRepository.save(instructor);

        Course course = new Course(instructor, name, dayOfWeek, startTime, endTime);
        courseRepository.save(course);
        return course;
    }

    private CourseHistory saveCourseHistory(Course course, LocalDate date, int count) {

        CourseHistory courseHistory = new CourseHistory(course, date, count);
        courseHistoryRepository.save(courseHistory);
        return courseHistory;
    }

    private Subscription saveSubscription(Member member, LocalDate startDate, LocalDate endDate, int reservedCount, int completedCount, int availableCount) {

        Subscription subscription = new Subscription(member, startDate, endDate, reservedCount, completedCount, availableCount);
        subscriptionRepository.save(subscription);
        return subscription;
    }
}