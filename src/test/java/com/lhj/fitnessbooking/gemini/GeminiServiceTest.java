package com.lhj.fitnessbooking.gemini;

import com.lhj.fitnessbooking.domain.course.domain.Course;
import com.lhj.fitnessbooking.domain.course.domain.DayOfWeek;
import com.lhj.fitnessbooking.domain.course.repository.CourseRepository;
import com.lhj.fitnessbooking.domain.coursehistory.domain.CourseHistory;
import com.lhj.fitnessbooking.domain.coursehistory.repository.CourseHistoryRepository;
import com.lhj.fitnessbooking.domain.gemini.dto.MemberInputRequest;
import com.lhj.fitnessbooking.domain.gemini.dto.RecommendDto;
import com.lhj.fitnessbooking.domain.gemini.service.GeminiService;
import com.lhj.fitnessbooking.domain.instructor.domain.Instructor;
import com.lhj.fitnessbooking.domain.instructor.repository.InstructorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.lhj.fitnessbooking.domain.course.domain.DayOfWeek.MON;
import static com.lhj.fitnessbooking.domain.gemini.dto.MemberCondition.GOOD;
import static com.lhj.fitnessbooking.domain.gemini.dto.MemberGoal.WEIGHT;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GeminiServiceTest {

    @Autowired
    GeminiService geminiService;

    @Autowired InstructorRepository instructorRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired CourseHistoryRepository courseHistoryRepository;

    @DisplayName("요가 수업 추천하기(1) - 오늘의 수업이 다 끝난 경우")
    @Test
    void recommendCourse1() {

        // given
        Instructor instructor = saveInstructor("유나");

        Course course1 = saveCourse(instructor, "하타", MON, LocalTime.of(9, 0), LocalTime.of(9, 50));
        Course course2 = saveCourse(instructor, "필라테스", MON, LocalTime.of(10, 0), LocalTime.of(10, 50));

        saveCourseHistory(course1, LocalDate.of(2025, 2, 17), 4);
        saveCourseHistory(course2, LocalDate.of(2025, 2, 17), 5);
        saveCourseHistory(course1, LocalDate.of(2025, 2, 24), 5);
        saveCourseHistory(course2, LocalDate.of(2025, 2, 24), 5);

        MemberInputRequest request = new MemberInputRequest(GOOD, WEIGHT);

        // when
        RecommendDto recommendDto = geminiService.recommendCourse(request);

        // then
        assertThat(recommendDto.getGeminiSaid()).isEqualTo("오늘의 수업이 존재하지 않습니다!");
    }

    @DisplayName("요가 수업 추천하기(2) - 오늘의 수업이 1개 남은 경우")
    @Test
    void recommendCourse2() {

        // given
        Instructor instructor = saveInstructor("유나");

        Course course1 = saveCourse(instructor, "하타", MON, LocalTime.of(8, 0), LocalTime.of(8, 50));
        Course course2 = saveCourse(instructor, "필라테스", MON, LocalTime.of(18, 0), LocalTime.of(18, 50));

        saveCourseHistory(course1, LocalDate.of(2025, 2, 17), 4);
        saveCourseHistory(course2, LocalDate.of(2025, 2, 17), 5);
        saveCourseHistory(course1, LocalDate.of(2025, 2, 24), 5);
        saveCourseHistory(course2, LocalDate.of(2025, 2, 24), 5);

        MemberInputRequest request = new MemberInputRequest(GOOD, WEIGHT);

        // when
        RecommendDto recommendDto = geminiService.recommendCourse(request);

        // then
        assertThat(recommendDto.getGeminiSaid()).isEqualTo("오늘의 수업이 1개 남았습니다. 해당 수업을 예약하시겠습니까?");
    }

    @DisplayName("요가 수업 추천하기(3) - 오늘의 수업이 2개 이상 남아서 Gemini가 추천할 수 있는 경우")
    @Test
    void recommendCourse3() {

        // given
        Instructor instructor = saveInstructor("유나");

        Course course1 = saveCourse(instructor, "하타", MON, LocalTime.of(18, 0), LocalTime.of(18, 50));
        Course course2 = saveCourse(instructor, "필라테스", MON, LocalTime.of(19, 0), LocalTime.of(19, 50));

        saveCourseHistory(course1, LocalDate.of(2025, 2, 17), 4);
        saveCourseHistory(course2, LocalDate.of(2025, 2, 17), 5);
        saveCourseHistory(course1, LocalDate.of(2025, 2, 24), 5);
        saveCourseHistory(course2, LocalDate.of(2025, 2, 24), 5);

        MemberInputRequest request = new MemberInputRequest(GOOD, WEIGHT);

        // when
        RecommendDto recommendDto = geminiService.recommendCourse(request);

        // then
        assertThat(recommendDto.getGeminiSaid()).isNotEmpty();
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

    private CourseHistory saveCourseHistory(Course course, LocalDate date, int count) {

        CourseHistory courseHistory = new CourseHistory(course, date, count);
        courseHistoryRepository.save(courseHistory);
        return courseHistory;
    }
}