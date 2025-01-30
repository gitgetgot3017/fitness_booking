package com.lhj.FitnessBooking.courseHistory;

import com.lhj.FitnessBooking.course.CourseRepository;
import com.lhj.FitnessBooking.course.exception.EnrollmentLimitExceededException;
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
import java.util.List;
import java.util.Optional;

import static com.lhj.FitnessBooking.domain.DayOfWeek.TUES;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class CourseHistoryRepositoryTest {

    @Autowired InstructorRepository instructorRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CourseHistoryRepository courseHistoryRepository;

    @DisplayName("하루 수강 횟수 확인")
    @Test
    public void findByDate() {

        // given
        Instructor instructor = saveInstructor("지수");
        Course course1 = saveCourse(instructor, "캐딜락", TUES, LocalTime.of(18, 0));
        Course course2 = saveCourse(instructor, "바렐", TUES, LocalTime.of(20, 0));

        courseHistoryRepository.save(new CourseHistory(course1, LocalDate.of(2025, 1, 30), 5));
        courseHistoryRepository.save(new CourseHistory(course2, LocalDate.of(2025, 1, 30), 6));

        // when
        List<CourseHistory> enrolledDates = courseHistoryRepository.findByDate(LocalDate.of(2025, 1, 30));

        // then
        assertThat(enrolledDates).hasSize(2);
    }

    @DisplayName("수강 신청 여부 확인 - 수강 신청하지 않은 경우")
    @Test
    public void findByCourseAndDateSuccess() {

        // given
        Instructor instructor = saveInstructor("지수");
        Course course1 = saveCourse(instructor, "캐딜락", TUES, LocalTime.of(18, 0));
        Course course2 = saveCourse(instructor, "바렐", TUES, LocalTime.of(20, 0));

        courseHistoryRepository.save(new CourseHistory(course1, LocalDate.of(2025, 1, 30), 5));

        // when
        Optional<CourseHistory> result = courseHistoryRepository.findByDateAndCourse(LocalDate.of(2025, 1, 30), course2);

        // then
        assertThat(result).isEmpty();
    }

    @DisplayName("수강 신청 여부 확인 - 이미 수강 신청한 경우")
    @Test
    public void findByCourseAndDateFail() {

        // given
        Instructor instructor = saveInstructor("지수");
        Course course = saveCourse(instructor, "캐딜락", TUES, LocalTime.of(18, 0));

        courseHistoryRepository.save(new CourseHistory(course, LocalDate.of(2025, 1, 30), 5));

        // when
        Optional<CourseHistory> result = courseHistoryRepository.findByDateAndCourse(LocalDate.of(2025, 1, 30), course);

        // then
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
}