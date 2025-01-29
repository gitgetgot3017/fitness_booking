package com.lhj.FitnessBooking.course;

import com.lhj.FitnessBooking.courseHistory.CourseHistoryRepository;
import com.lhj.FitnessBooking.domain.Course;
import com.lhj.FitnessBooking.domain.CourseHistory;
import com.lhj.FitnessBooking.domain.DayOfWeek;
import com.lhj.FitnessBooking.domain.Instructor;
import com.lhj.FitnessBooking.dto.CourseInfoTmp;
import com.lhj.FitnessBooking.instructor.InstructorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.lhj.FitnessBooking.domain.DayOfWeek.TUES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
class CourseRepositoryTest {

    @Autowired InstructorRepository instructorRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CourseHistoryRepository courseHistoryRepository;

    @AfterEach
    void after() {
        courseHistoryRepository.deleteAll();
    }

    @DisplayName("홈 > 그룹예약: 오늘의 수강 목록 조회")
    @Test
    void getTodayCourses() {

        // given
        Instructor instructor1 = saveInstructor("지수");
        Instructor instructor2 = saveInstructor("세미");

        Course course1 = saveCourse(instructor1, "캐딜락", TUES, LocalTime.of(9, 0));
        Course course2 = saveCourse(instructor1, "바렐", TUES, LocalTime.of(9, 0));
        Course course3 = saveCourse(instructor1, "리포머", TUES, LocalTime.of(18, 0));
        Course course4 = saveCourse(instructor2, "체어", TUES, LocalTime.of(18, 0));

        courseHistoryRepository.save(new CourseHistory(course1, LocalDate.of(2025, 1, 26), 6));
        courseHistoryRepository.save(new CourseHistory(course1, LocalDate.of(2025, 1, 26), 5));
        courseHistoryRepository.save(new CourseHistory(course2, LocalDate.of(2025, 1, 26), 6));
        courseHistoryRepository.save(new CourseHistory(course2, LocalDate.of(2025, 1, 26), 4));
        courseHistoryRepository.save(new CourseHistory(course3, LocalDate.of(2025, 1, 26), 5));
        courseHistoryRepository.save(new CourseHistory(course3, LocalDate.of(2025, 1, 26), 6));
        courseHistoryRepository.save(new CourseHistory(course3, LocalDate.of(2025, 1, 26), 6));
        courseHistoryRepository.save(new CourseHistory(course4, LocalDate.of(2025, 1, 26), 5));
        courseHistoryRepository.save(new CourseHistory(course4, LocalDate.of(2025, 1, 26), 4));
        courseHistoryRepository.save(new CourseHistory(course4, LocalDate.of(2025, 1, 26), 5));

        // when
        List<CourseInfoTmp> courseInfoList = courseRepository.getTodayCourses(LocalDate.of(2025, 1, 26), LocalTime.of(12, 30));

        // then
        assertThat(courseInfoList).hasSize(6)
                .extracting("instructorName", "courseName", "courseStartTime", "attendeeCount")
                .containsExactlyInAnyOrder(
                        tuple("지수", "리포머", LocalTime.of(18, 0), 5),
                        tuple("지수", "리포머", LocalTime.of(18, 0), 6),
                        tuple("지수", "리포머", LocalTime.of(18, 0), 6),
                        tuple("세미", "체어", LocalTime.of(18, 0), 5),
                        tuple("세미", "체어", LocalTime.of(18, 0), 4),
                        tuple("세미", "체어", LocalTime.of(18, 0), 5)
                );
    }

    @DisplayName("홈 > 그룹예약: 내일의 수강 목록 조회")
    @Test
    void getTomorrowCourses() {

        // given
        Instructor instructor1 = saveInstructor("지수");
        Instructor instructor2 = saveInstructor("세미");

        Course course1 = saveCourse(instructor1, "캐딜락", TUES, LocalTime.of(9, 0));
        Course course2 = saveCourse(instructor1, "바렐", TUES, LocalTime.of(9, 0));
        Course course3 = saveCourse(instructor1, "리포머", TUES, LocalTime.of(18, 0));
        Course course4 = saveCourse(instructor2, "체어", TUES, LocalTime.of(18, 0));

        courseHistoryRepository.save(new CourseHistory(course1, LocalDate.of(2025, 1, 26), 6));
        courseHistoryRepository.save(new CourseHistory(course1, LocalDate.of(2025, 1, 26), 5));
        courseHistoryRepository.save(new CourseHistory(course2, LocalDate.of(2025, 1, 26), 6));
        courseHistoryRepository.save(new CourseHistory(course2, LocalDate.of(2025, 1, 26), 4));
        courseHistoryRepository.save(new CourseHistory(course3, LocalDate.of(2025, 1, 26), 5));
        courseHistoryRepository.save(new CourseHistory(course3, LocalDate.of(2025, 1, 26), 6));
        courseHistoryRepository.save(new CourseHistory(course3, LocalDate.of(2025, 1, 26), 6));
        courseHistoryRepository.save(new CourseHistory(course4, LocalDate.of(2025, 1, 26), 5));
        courseHistoryRepository.save(new CourseHistory(course4, LocalDate.of(2025, 1, 26), 4));
        courseHistoryRepository.save(new CourseHistory(course4, LocalDate.of(2025, 1, 26), 5));

        // when
        List<CourseInfoTmp> courseInfoList = courseRepository.getTomorrowCourses(LocalDate.of(2025, 1, 26));

        // then
        assertThat(courseInfoList).hasSize(10)
                .extracting("instructorName", "courseName", "courseStartTime", "attendeeCount")
                .containsExactlyInAnyOrder(
                        tuple("지수", "캐딜락", LocalTime.of(9, 0), 6),
                        tuple("지수", "캐딜락", LocalTime.of(9, 0), 5),
                        tuple("지수", "바렐", LocalTime.of(9, 0), 6),
                        tuple("지수", "바렐", LocalTime.of(9, 0), 4),
                        tuple("지수", "리포머", LocalTime.of(18, 0), 5),
                        tuple("지수", "리포머", LocalTime.of(18, 0), 6),
                        tuple("지수", "리포머", LocalTime.of(18, 0), 6),
                        tuple("세미", "체어", LocalTime.of(18, 0), 5),
                        tuple("세미", "체어", LocalTime.of(18, 0), 4),
                        tuple("세미", "체어", LocalTime.of(18, 0), 5)
                );
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