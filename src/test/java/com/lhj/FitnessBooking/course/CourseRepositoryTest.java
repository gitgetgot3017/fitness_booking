package com.lhj.FitnessBooking.course;

import com.lhj.FitnessBooking.domain.Course;
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

import static com.lhj.FitnessBooking.domain.DayOfWeek.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
class CourseRepositoryTest {

    @Autowired InstructorRepository instructorRepository;
    @Autowired CourseRepository courseRepository;

    @DisplayName("홈 > 그룹예약: 수강 목록 조회")
    @Test
    public void getCourses() {

        // given
        Instructor instructor1 = new Instructor("지수");
        instructorRepository.save(instructor1);

        Instructor instructor2 = new Instructor("세미");
        instructorRepository.save(instructor2);

        Course course1 = new Course(instructor1, "캐딜락", MON, LocalTime.of(9, 0));
        courseRepository.save(course1);

        Course course2 = new Course(instructor1, "바렐", TUES, LocalTime.of(9, 0));
        courseRepository.save(course2);

        Course course3 = new Course(instructor1, "리포머", TUES, LocalTime.of(18, 0));
        courseRepository.save(course3);

        Course course4 = new Course(instructor2, "체어", TUES, LocalTime.of(18, 0));
        courseRepository.save(course4);

        // when
        List<Course> courses = courseRepository.getCourses(TUES, LocalTime.of(12, 30));

        // then
        assertThat(courses).hasSize(2)
                .extracting("name", "startTime")
                .containsExactlyInAnyOrder(
                        tuple("리포머", LocalTime.of(18, 0)),
                        tuple("체어", LocalTime.of(18, 0))
                );
    }
}