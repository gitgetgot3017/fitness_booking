package com.lhj.FitnessBooking.history;

import com.lhj.FitnessBooking.course.CourseRepository;
import com.lhj.FitnessBooking.courseHistory.CourseHistoryRepository;
import com.lhj.FitnessBooking.domain.*;
import com.lhj.FitnessBooking.dto.CheckBefore4HourDto;
import com.lhj.FitnessBooking.instructor.InstructorRepository;
import com.lhj.FitnessBooking.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.lhj.FitnessBooking.domain.CourseStatus.*;
import static com.lhj.FitnessBooking.domain.DayOfWeek.MON;
import static com.lhj.FitnessBooking.domain.DayOfWeek.TUES;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HistoryRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired InstructorRepository instructorRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired HistoryRepository historyRepository;
    @Autowired CourseHistoryRepository courseHistoryRepository;

    @DisplayName("홈 > 그룹예약: 수강 및 예약 날짜 조회(1)")
    @Test
    void getHistoryTest1() {

        // given
        Member member = saveMember("2073", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));

        Instructor instructor = new Instructor("지수");
        instructorRepository.save(instructor);

        Course course = new Course(instructor, "캐딜락", MON, LocalTime.of(9, 0));
        courseRepository.save(course);

        int year = 2025;
        int month = 1;

        historyRepository.save(new History(member, LocalDate.of(year, month, 26), course, year, month, LocalDateTime.of(2025, 1, 26, 8, 0), CANCELED));
        historyRepository.save(new History(member, LocalDate.of(year, month, 26), course, year, month, LocalDateTime.of(2025, 1, 26, 9, 0), RESERVED));

        // when
        List<History> historyList = historyRepository.getHistory(member, year, month);

        // then
        assertThat(historyList).hasSize(1);
    }

    @DisplayName("홈 > 그룹예약: 수강 및 예약 날짜 조회(2)")
    @Test
    void getHistoryTest2() {

        // given
        Member member = saveMember("2073", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));

        Instructor instructor = new Instructor("지수");
        instructorRepository.save(instructor);

        Course course = new Course(instructor, "캐딜락", MON, LocalTime.of(9, 0));
        courseRepository.save(course);

        int year = 2025;
        int month = 1;

        historyRepository.save(new History(member, LocalDate.of(year, month, 26), course, year, month, LocalDateTime.of(2025, 1, 26, 8, 0), ENROLLED));
        historyRepository.save(new History(member, LocalDate.of(year, month, 26), course, year, month, LocalDateTime.of(2025, 1, 26, 9, 0), RESERVED));

        // when
        List<History> historyList = historyRepository.getHistory(member, year, month);

        // then
        assertThat(historyList).hasSize(2);
    }

    @DisplayName("홈 > 그룹예약: 수강 및 예약 날짜 조회(3)")
    @Test
    public void getHistoryTest3() {

        // given
        Member member = saveMember("2073", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));

        Instructor instructor = new Instructor("지수");
        instructorRepository.save(instructor);

        Course course = new Course(instructor, "캐딜락", MON, LocalTime.of(9, 0));
        courseRepository.save(course);

        int year = 2025;
        int month = 1;

        historyRepository.save(new History(member, LocalDate.of(year, month, 26), course, year, month, LocalDateTime.of(2025, 1, 26, 8, 0), RESERVED));
        historyRepository.save(new History(member, LocalDate.of(year, month, 26), course, year, month, LocalDateTime.of(2025, 1, 26, 9, 0), CANCELED));

        // when
        List<History> historyList = historyRepository.getHistory(member, year, month);

        // then
        assertThat(historyList).hasSize(1);
    }

    @DisplayName("홈 > 그룹예약: 수강 및 예약 날짜 조회(4)")
    @Test
    public void getHistoryTest4() {

        // given
        Member member = saveMember("2073", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));

        Instructor instructor = new Instructor("지수");
        instructorRepository.save(instructor);

        Course course = new Course(instructor, "캐딜락", MON, LocalTime.of(9, 0));
        courseRepository.save(course);

        int year = 2025;
        int month = 1;

        historyRepository.save(new History(member, LocalDate.of(year, month, 26), course, year, month, LocalDateTime.of(2025, 1, 26, 8, 0), RESERVED));
        historyRepository.save(new History(member, LocalDate.of(year, month, 26), course, year, month, LocalDateTime.of(2025, 1, 26, 9, 0), ENROLLED));

        // when
        List<History> historyList = historyRepository.getHistory(member, year, month);

        // then
        assertThat(historyList).hasSize(2);
    }

    @DisplayName("홈 > 그룹예약: 수강 및 예약 날짜 조회(5)")
    @Test
    public void getHistoryTest5() {

        // given
        Member member1 = saveMember("2073", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));
        Member member2 = saveMember("2073", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));

        Instructor instructor = new Instructor("지수");
        instructorRepository.save(instructor);

        Course course = new Course(instructor, "캐딜락", MON, LocalTime.of(9, 0));
        courseRepository.save(course);

        int year = 2025;
        int month = 1;

        historyRepository.save(new History(member1, LocalDate.of(year, month, 26), course, year, month, LocalDateTime.of(2025, 1, 26, 8, 0), RESERVED));
        historyRepository.save(new History(member2, LocalDate.of(year, month, 26), course, year, month, LocalDateTime.of(2025, 1, 26, 9, 0), CANCELED));
        historyRepository.save(new History(member2, LocalDate.of(year, month, 26), course, year, month, LocalDateTime.of(2025, 1, 26, 9, 0), RESERVED));

        // when
        List<History> historyList = historyRepository.getHistory(member1, year, month);

        // then
        assertThat(historyList).hasSize(1);
    }

    @DisplayName("수강 취소 횟수 구하기")
    @Test
    void getCancelCount() {

        // given
        Member member = saveMember("2073", "이현지", "01062802073", false, LocalDate.of(2025, 1, 31));
        Course course1 = saveCourse("캐딜락", TUES, LocalTime.of(18, 0));
        Course course2 = saveCourse("바렐", TUES, LocalTime.of(20, 0));

        historyRepository.save(new History(member, LocalDate.of(2025, 1, 26), course1, 2025, 1, LocalDateTime.of(2025, 1, 31, 16, 29), RESERVED));
        historyRepository.save(new History(member, LocalDate.of(2025, 1, 26), course1, 2025, 1, LocalDateTime.of(2025, 1, 31, 16, 29), CANCELED));
        historyRepository.save(new History(member, LocalDate.of(2025, 1, 26), course2, 2025, 1, LocalDateTime.of(2025, 1, 31, 16, 29), RESERVED));
        historyRepository.save(new History(member, LocalDate.of(2025, 1, 26), course2, 2025, 1, LocalDateTime.of(2025, 2, 1, 16, 29), CANCELED));

        // when
        List<History> cancelCount = historyRepository.getCancelCount(member, LocalDate.of(2025, 1, 31));

        // then
        assertThat(cancelCount).hasSize(1);
    }

    @DisplayName("수업 시작 4시간 전 취소 - 성공 케이스")
    @Test
    void ifBefore4hourSuccess() {

        // given
        Member member = saveMember("2073", "이현지", "01062802073", false, LocalDate.of(2025, 1, 31));
        Course course = saveCourse("캐딜락", TUES, LocalTime.of(18, 0));
        saveHistory(member, course, 2025, 1, LocalDateTime.of(2025, 1, 30, 8, 0), RESERVED);
        saveCourseHistory(course, LocalDate.of(2025, 1, 31), 5);

        // when
        Optional<CheckBefore4HourDto> ifBefore4Hour = historyRepository.ifBefore4hour(member, LocalDate.of(2025, 1, 31), course, LocalTime.of(13, 59, 59).plusHours(4));

        // then
        assertThat(ifBefore4Hour).isNotEmpty();
    }

    @DisplayName("수업 시작 4시간 전 취소 - 실패 케이스")
    @Test
    void ifBefore4hourFail() {

        // given
        Member member = saveMember("2073", "이현지", "01062802073", false, LocalDate.of(2025, 1, 31));
        Course course = saveCourse("캐딜락", TUES, LocalTime.of(18, 0));
        saveHistory(member, course, 2025, 1, LocalDateTime.of(2025, 1, 30, 8, 0), RESERVED);
        saveCourseHistory(course, LocalDate.of(2025, 1, 31), 5);

        // when
        Optional<CheckBefore4HourDto> ifBefore4Hour = historyRepository.ifBefore4hour(member, LocalDate.of(2025, 1, 31), course, LocalTime.of(14, 0).plusHours(4));

        // then
        assertThat(ifBefore4Hour).isEmpty();
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

    private History saveHistory(Member member, Course course, int year, int month, LocalDateTime regDateTime, CourseStatus status) {

        History history = new History(member, LocalDate.of(year, month, 26), course, year, month, regDateTime, status);
        historyRepository.save(history);
        return history;
    }

    private CourseHistory saveCourseHistory(Course course, LocalDate date, int count) {

        CourseHistory courseHistory = new CourseHistory(course, date, count);
        courseHistoryRepository.save(courseHistory);
        return courseHistory;
    }
}