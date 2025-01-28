package com.lhj.FitnessBooking.course;

import com.lhj.FitnessBooking.courseHistory.CourseHistoryRepository;
import com.lhj.FitnessBooking.domain.Course;
import com.lhj.FitnessBooking.domain.CourseHistory;
import com.lhj.FitnessBooking.domain.Instructor;
import com.lhj.FitnessBooking.dto.CourseInfoTmp;
import com.lhj.FitnessBooking.instructor.InstructorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.lhj.FitnessBooking.domain.DayOfWeek.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class CourseRepositoryTest {

    @Autowired InstructorRepository instructorRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CourseHistoryRepository courseHistoryRepository;

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

        courseHistoryRepository.save(new CourseHistory(course1, LocalDateTime.of(2025, 1, 26, 9, 0), 6));
        courseHistoryRepository.save(new CourseHistory(course2, LocalDateTime.of(2025, 1, 26, 9, 0), 5));
        courseHistoryRepository.save(new CourseHistory(course3, LocalDateTime.of(2025, 1, 26, 18, 0), 6));
        courseHistoryRepository.save(new CourseHistory(course3, LocalDateTime.of(2025, 1, 26, 18, 0), 4));
        courseHistoryRepository.save(new CourseHistory(course3, LocalDateTime.of(2025, 1, 26, 18, 0), 5));
        courseHistoryRepository.save(new CourseHistory(course3, LocalDateTime.of(2025, 1, 26, 18, 0), 6));
        courseHistoryRepository.save(new CourseHistory(course4, LocalDateTime.of(2025, 1, 26, 18, 0), 6));
        courseHistoryRepository.save(new CourseHistory(course4, LocalDateTime.of(2025, 1, 26, 18, 0), 5));
        courseHistoryRepository.save(new CourseHistory(course4, LocalDateTime.of(2025, 1, 26, 18, 0), 4));
        courseHistoryRepository.save(new CourseHistory(course4, LocalDateTime.of(2025, 1, 26, 18, 0), 5));

        // when
        List<CourseInfoTmp> courseInfoList = courseRepository.getCourses(1, TUES, LocalTime.of(12, 30));

        // then
        assertThat(courseInfoList).hasSize(4)
                .extracting("instructorName", "courseName", "courseStartTime", "attendeeCount")
                .containsExactlyInAnyOrder(
                        tuple("지수", "리포머", LocalTime.of(18, 0), 6),
                        tuple("지수", "리포머", LocalTime.of(18, 0), 4),
                        tuple("세미", "체어", LocalTime.of(18, 0), 6),
                        tuple("세미", "체어", LocalTime.of(18, 0), 5)
                );
    }
}