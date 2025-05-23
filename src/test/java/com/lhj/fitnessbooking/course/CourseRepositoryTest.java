package com.lhj.fitnessbooking.course;

import com.lhj.fitnessbooking.domain.course.domain.Course;
import com.lhj.fitnessbooking.domain.course.domain.DayOfWeek;
import com.lhj.fitnessbooking.domain.course.dto.CourseInfoTmp;
import com.lhj.fitnessbooking.domain.course.exception.NotExistCourseException;
import com.lhj.fitnessbooking.domain.course.repository.CourseRepository;
import com.lhj.fitnessbooking.domain.coursehistory.domain.CourseHistory;
import com.lhj.fitnessbooking.domain.coursehistory.repository.CourseHistoryRepository;
import com.lhj.fitnessbooking.domain.instructor.domain.Instructor;
import com.lhj.fitnessbooking.domain.instructor.repository.InstructorRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.lhj.fitnessbooking.domain.course.domain.DayOfWeek.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
@Transactional
class CourseRepositoryTest {

    @Autowired EntityManager em;

    @Autowired InstructorRepository instructorRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CourseHistoryRepository courseHistoryRepository;

    @AfterEach
    void after() {
        courseHistoryRepository.deleteAll();
    }

    @DisplayName("홈 > 그룹예약: 수강 목록 조회")
    @Test
    void getTodayCourses() {

        // given
        Instructor instructor1 = saveInstructor("지수");
        Instructor instructor2 = saveInstructor("세미");

        Course course1 = saveCourse(instructor1, "캐딜락", TUES, LocalTime.of(9, 0), LocalTime.of(0, 0));
        Course course2 = saveCourse(instructor1, "바렐", TUES, LocalTime.of(9, 0), LocalTime.of(0, 0));
        Course course3 = saveCourse(instructor1, "리포머", TUES, LocalTime.of(18, 0), LocalTime.of(0, 0));
        Course course4 = saveCourse(instructor2, "체어", TUES, LocalTime.of(18, 0), LocalTime.of(0, 0));

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
        List<CourseInfoTmp> courseInfoList = courseRepository.getCourses(LocalDate.of(2025, 1, 26), LocalTime.of(12, 30));

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

    @DisplayName("수강 정원 구하기")
    @Test
    void getCourseCount() {

        // given
        Instructor instructor = saveInstructor("지수");

        Course course = courseRepository.save(new Course(instructor, "캐딜락", TUES, LocalTime.of(18, 0), LocalTime.of(0, 0)));
        courseHistoryRepository.save(new CourseHistory(course, LocalDate.of(2025, 1, 30), 5));

        // when
        int courseCount = courseRepository.getCourseCount(LocalDate.of(2025, 1, 30), course.getId());

        // then
        assertThat(courseCount).isEqualTo(5);
    }

    @DisplayName("홈>그룹예약>예약상세: 수강 정보 확인")
    @Test
    void getCourseDetailCourseInfo() {

        // given
        Instructor instructor = saveInstructor("지수");
        Course course = courseRepository.save(new Course(instructor, "캐딜락", SAT, LocalTime.of(11, 0), LocalTime.of(0, 0)));
        courseHistoryRepository.save(new CourseHistory(course, LocalDate.of(2025, 2, 1), 6));

        // when, then
        courseRepository.getCourseDetailCourseInfo(LocalDate.of(2025, 2, 1), course.getId())
                .isPresent();
    }

    @DisplayName("수강 인원 1명 늘리기")
    @Test
    void increaseCourseCount() {

        // given
        Instructor instructor = saveInstructor("지수");
        Course course = courseRepository.save(new Course(instructor, "캐딜락", SAT, LocalTime.of(11, 0), LocalTime.of(0, 0)));
        CourseHistory courseHistory = courseHistoryRepository.save(new CourseHistory(course, LocalDate.of(2025, 2, 2), 4));

        // when
        courseRepository.increaseCourseCount(LocalDate.of(2025, 2, 2), course);

        em.clear();

        // then
        CourseHistory updatedCourseHistory = courseHistoryRepository.findById(courseHistory.getId())
                .orElseThrow(() -> new NotExistCourseException("해당 수업은 존재하지 않습니다."));
        assertThat(updatedCourseHistory.getCount()).isEqualTo(5);
    }

    @DisplayName("수강 인원 1명 줄이기")
    @Test
    void decreaseCourseCount() {

        // given
        Instructor instructor = saveInstructor("지수");
        Course course = courseRepository.save(new Course(instructor, "캐딜락", SAT, LocalTime.of(11, 0), LocalTime.of(0, 0)));
        CourseHistory courseHistory = courseHistoryRepository.save(new CourseHistory(course, LocalDate.of(2025, 2, 2), 4));

        // when
        courseRepository.decreaseCourseCount(LocalDate.of(2025, 2, 2), course);

        em.clear();

        // then
        CourseHistory updatedCourseHistory = courseHistoryRepository.findById(courseHistory.getId())
                .orElseThrow(() -> new NotExistCourseException("해당 수업은 존재하지 않습니다."));
        assertThat(updatedCourseHistory.getCount()).isEqualTo(3);
    }

    @DisplayName("특정 요일의 수업 전부 가져오기")
    @Test
    void findByDayOfWeek() {

        // given
        Instructor instructor = saveInstructor("지수");

        courseRepository.save(new Course(instructor, "캐딜락", WED, LocalTime.of(18, 0), LocalTime.of(0, 0)));
        courseRepository.save(new Course(instructor, "캐딜락", WED, LocalTime.of(19, 0), LocalTime.of(0, 0)));
        courseRepository.save(new Course(instructor, "캐딜락", WED, LocalTime.of(20, 0), LocalTime.of(0, 0)));
        courseRepository.save(new Course(instructor, "캐딜락", THUR, LocalTime.of(18, 0), LocalTime.of(0, 0)));
        courseRepository.save(new Course(instructor, "캐딜락", SAT, LocalTime.of(10, 0), LocalTime.of(0, 0)));

        // when
        List<Course> courses = courseRepository.findByDayOfWeek(WED);

        // then
        assertThat(courses).hasSize(3);
    }

    @DisplayName("특정 요일, 특정 이름의 수업 찾기")
    @Test
    void findByDayOfWeekAndName() {

        // given
        Instructor instructor = saveInstructor("써니");
        courseRepository.save(new Course(instructor, "플라잉", TUES, LocalTime.of(11, 0), LocalTime.of(0, 0)));

        // when
        Optional<Course> courseExist = courseRepository.findByDayOfWeekAndName(TUES, "플라잉");

        // then
        courseExist.isPresent();
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