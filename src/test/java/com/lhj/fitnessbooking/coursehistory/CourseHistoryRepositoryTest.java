package com.lhj.fitnessbooking.coursehistory;

import com.lhj.fitnessbooking.domain.course.domain.Course;
import com.lhj.fitnessbooking.domain.course.domain.DayOfWeek;
import com.lhj.fitnessbooking.domain.course.repository.CourseRepository;
import com.lhj.fitnessbooking.domain.coursehistory.domain.CourseHistory;
import com.lhj.fitnessbooking.domain.coursehistory.repository.CourseHistoryRepository;
import com.lhj.fitnessbooking.domain.history.domain.History;
import com.lhj.fitnessbooking.domain.history.repository.HistoryRepository;
import com.lhj.fitnessbooking.domain.instructor.domain.Instructor;
import com.lhj.fitnessbooking.domain.instructor.repository.InstructorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.lhj.fitnessbooking.domain.course.domain.DayOfWeek.TUES;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CourseHistoryRepositoryTest {

    @Autowired InstructorRepository instructorRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CourseHistoryRepository courseHistoryRepository;
    @Autowired HistoryRepository historyRepository;

    @DisplayName("하루 수강 횟수 확인")
    @Test
    public void findByDate() {

        // given
        Instructor instructor = saveInstructor("지수");
        Course course1 = saveCourse(instructor, "캐딜락", TUES, LocalTime.of(18, 0), LocalTime.of(0, 0));
        Course course2 = saveCourse(instructor, "바렐", TUES, LocalTime.of(20, 0), LocalTime.of(0, 0));

        courseHistoryRepository.save(new CourseHistory(course1, LocalDate.of(2025, 1, 30), 5));
        courseHistoryRepository.save(new CourseHistory(course2, LocalDate.of(2025, 1, 30), 6));

        // when
        List<History> enrolledDates = historyRepository.getReservedAndEnrolled(LocalDate.of(2025, 1, 30));

        // then
        assertThat(enrolledDates).hasSize(2);
    }

    @DisplayName("수강 신청 여부 확인 - 수강 신청하지 않은 경우")
    @Test
    public void findByCourseAndDateSuccess() {

        // given
        Instructor instructor = saveInstructor("지수");
        Course course1 = saveCourse(instructor, "캐딜락", TUES, LocalTime.of(18, 0), LocalTime.of(0, 0));
        Course course2 = saveCourse(instructor, "바렐", TUES, LocalTime.of(20, 0), LocalTime.of(0, 0));

        courseHistoryRepository.save(new CourseHistory(course1, LocalDate.of(2025, 1, 30), 5));

        // when
//        Optional<History> result = historyRepository.checkAlreadyRegistered(LocalDate.of(2025, 1, 30), course2);

        // then
//        assertThat(result).isEmpty();
    }

    @DisplayName("수강 신청 여부 확인 - 이미 수강 신청한 경우")
    @Test
    public void findByCourseAndDateFail() {

        // given
        Instructor instructor = saveInstructor("지수");
        Course course = saveCourse(instructor, "캐딜락", TUES, LocalTime.of(18, 0), LocalTime.of(0, 0));

        courseHistoryRepository.save(new CourseHistory(course, LocalDate.of(2025, 1, 30), 5));

        // when
//        Optional<History> result = historyRepository.checkAlreadyRegistered(LocalDate.of(2025, 1, 30), course);

        // then
//        assertThat(result).isNotEmpty();
    }

    private Instructor saveInstructor(String name) {
        Instructor instructor = new Instructor(name);
        instructorRepository.save(instructor);
        return instructor;
    }

    private Course saveCourse(Instructor instructor, String name, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        Course course = new Course(instructor, name, dayOfWeek, startTime, endTime);
        courseRepository.save(course);
        return course;
    }
}