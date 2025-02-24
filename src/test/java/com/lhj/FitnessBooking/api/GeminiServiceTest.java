package com.lhj.FitnessBooking.api;

import com.lhj.FitnessBooking.api.dto.MemberCondition;
import com.lhj.FitnessBooking.api.dto.MemberGoal;
import com.lhj.FitnessBooking.api.dto.MemberInputRequest;
import com.lhj.FitnessBooking.course.CourseRepository;
import com.lhj.FitnessBooking.courseHistory.CourseHistoryRepository;
import com.lhj.FitnessBooking.domain.Course;
import com.lhj.FitnessBooking.domain.CourseHistory;
import com.lhj.FitnessBooking.domain.DayOfWeek;
import com.lhj.FitnessBooking.domain.Instructor;
import com.lhj.FitnessBooking.instructor.InstructorRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.lhj.FitnessBooking.api.dto.MemberCondition.*;
import static com.lhj.FitnessBooking.api.dto.MemberGoal.*;
import static com.lhj.FitnessBooking.domain.DayOfWeek.MON;
import static com.lhj.FitnessBooking.domain.DayOfWeek.TUES;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class GeminiServiceTest {

    @Autowired GeminiService geminiService;

    @Autowired InstructorRepository instructorRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CourseHistoryRepository courseHistoryRepository;

    @DisplayName("요가 수업 추천하기(1) - 오늘의 수업이 다 끝난 경우")
    @Test
    void recommendCourse1() {

        // given
        Instructor instructor = saveInstructor("유나");

        Course course1 = saveCourse(instructor, "하타", MON, LocalTime.of(9, 0));
        Course course2 = saveCourse(instructor, "필라테스", MON, LocalTime.of(10, 0));

        saveCourseHistory(course1, LocalDate.of(2025, 2, 17), 4);
        saveCourseHistory(course2, LocalDate.of(2025, 2, 17), 5);
        saveCourseHistory(course1, LocalDate.of(2025, 2, 24), 5);
        saveCourseHistory(course2, LocalDate.of(2025, 2, 24), 5);

        MemberInputRequest request = new MemberInputRequest(GOOD, WEIGHT);

        // when
        String result = geminiService.recommendCourse(request);

        // then
        assertThat(result).isEqualTo("오늘의 수업이 존재하지 않습니다!");
    }

    @DisplayName("요가 수업 추천하기(2) - 오늘의 수업이 1개 남은 경우")
    @Test
    void recommendCourse2() {

        // given
        Instructor instructor = saveInstructor("유나");

        Course course1 = saveCourse(instructor, "하타", MON, LocalTime.of(8, 0));
        Course course2 = saveCourse(instructor, "필라테스", MON, LocalTime.of(18, 0));

        saveCourseHistory(course1, LocalDate.of(2025, 2, 17), 4);
        saveCourseHistory(course2, LocalDate.of(2025, 2, 17), 5);
        saveCourseHistory(course1, LocalDate.of(2025, 2, 24), 5);
        saveCourseHistory(course2, LocalDate.of(2025, 2, 24), 5);

        MemberInputRequest request = new MemberInputRequest(GOOD, WEIGHT);

        // when
        String result = geminiService.recommendCourse(request);

        // then
        assertThat(result).isEqualTo("오늘의 수업이 1개 남았습니다. 해당 수업을 예약하시겠습니까?");
    }

    @DisplayName("요가 수업 추천하기(3) - 오늘의 수업이 2개 이상 남아서 Gemini가 추천할 수 있는 경우")
    @Test
    void recommendCourse3() {

        // given
        Instructor instructor = saveInstructor("유나");

        Course course1 = saveCourse(instructor, "하타", MON, LocalTime.of(18, 0));
        Course course2 = saveCourse(instructor, "필라테스", MON, LocalTime.of(19, 0));

        saveCourseHistory(course1, LocalDate.of(2025, 2, 17), 4);
        saveCourseHistory(course2, LocalDate.of(2025, 2, 17), 5);
        saveCourseHistory(course1, LocalDate.of(2025, 2, 24), 5);
        saveCourseHistory(course2, LocalDate.of(2025, 2, 24), 5);

        MemberInputRequest request = new MemberInputRequest(GOOD, WEIGHT);

        // when
        String result = geminiService.recommendCourse(request);

        // then
        System.out.println("result = " + result);
        assertThat(result).isNotEmpty();
    }

    private Instructor saveInstructor(String name) {
        Instructor instructor = new Instructor(name);
        instructorRepository.save(instructor);
        return instructor;
    }

    private Course saveCourse(Instructor instructor, String name, DayOfWeek dayOfWeek, LocalTime startTime) {
        Course course = new Course(instructor, name, dayOfWeek, startTime);
        courseRepository.save(course);
        return course;
    }

    private CourseHistory saveCourseHistory(Course course, LocalDate date, int count) {

        CourseHistory courseHistory = new CourseHistory(course, date, count);
        courseHistoryRepository.save(courseHistory);
        return courseHistory;
    }
}