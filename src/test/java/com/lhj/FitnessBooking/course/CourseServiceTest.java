package com.lhj.FitnessBooking.course;

import com.lhj.FitnessBooking.courseHistory.CourseHistoryRepository;
import com.lhj.FitnessBooking.domain.*;
import com.lhj.FitnessBooking.dto.CourseMainHeader;
import com.lhj.FitnessBooking.history.HistoryRepository;
import com.lhj.FitnessBooking.instructor.InstructorRepository;
import com.lhj.FitnessBooking.member.MemberRepository;
import com.lhj.FitnessBooking.subscription.SubscriptionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.lhj.FitnessBooking.domain.CourseStatus.RESERVED;
import static com.lhj.FitnessBooking.domain.DayOfWeek.TUES;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CourseServiceTest {

    @Autowired MemberRepository memberRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired InstructorRepository instructorRepository;
    @Autowired CourseHistoryRepository courseHistoryRepository;
    @Autowired HistoryRepository historyRepository;
    @Autowired SubscriptionRepository subscriptionRepository;

    @Autowired CourseService courseService;

    @DisplayName("수강 예약하기: 예약에 성공하는 경우")
    @Test
    void reserveCourseSuccess() {

        // given
        Course course = saveCourse("캐딜락", TUES, LocalTime.of(18, 0));
        CourseHistory courseHistory = saveCourseHistory(course, LocalDate.of(2025, 2, 4), 5);

        Member member = saveMember("2073", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));
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

        Course course = saveCourse("캐딜락", TUES, LocalTime.of(18, 0));
        CourseHistory courseHistory = saveCourseHistory(course, LocalDate.of(2025, 2, 4), 6);

        Member member = saveMember("2073", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));
        LocalDate courseDate = LocalDate.of(2025, 2, 4);
        Long courseId = course.getId();

        saveSubscription(member, LocalDate.of(2024, 6, 18), LocalDate.of(2025, 2, 23), 2, 73, 77);
        CourseMainHeader subscription = subscriptionRepository.getSubscription(member, courseDate);
        int reservedCount = subscription.getReservedCount();

        // when
        courseService.reserveCourse(member, courseDate, courseId);

        // then
        int courseCount = courseRepository.getCourseCount(courseDate, courseId);
        assertThat(courseCount).isEqualTo(courseHistory.getCount());

        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(0);

        assertThat(subscriptionRepository.getSubscription(member, courseDate).getReservedCount()).isEqualTo(reservedCount);
    }

    private Member saveMember(String memberNum, String name, String phone, boolean gender, LocalDate regDate) {

        Member member = new Member(memberNum, name, phone, gender, regDate);
        memberRepository.save(member);
        return member;
    }

    private Course saveCourse(String name, DayOfWeek dayOfWeek, LocalTime startTime) {

        Instructor instructor = new Instructor(name);
        instructorRepository.save(instructor);

        Course course = new Course(instructor, name, dayOfWeek, startTime);
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