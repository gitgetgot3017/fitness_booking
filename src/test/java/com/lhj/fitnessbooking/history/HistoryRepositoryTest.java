package com.lhj.fitnessbooking.history;

import com.lhj.fitnessbooking.domain.course.domain.Course;
import com.lhj.fitnessbooking.domain.course.domain.DayOfWeek;
import com.lhj.fitnessbooking.domain.course.dto.CourseHistoryTmp;
import com.lhj.fitnessbooking.domain.course.repository.CourseRepository;
import com.lhj.fitnessbooking.domain.coursehistory.domain.CourseHistory;
import com.lhj.fitnessbooking.domain.coursehistory.repository.CourseHistoryRepository;
import com.lhj.fitnessbooking.domain.history.domain.CourseStatus;
import com.lhj.fitnessbooking.domain.history.domain.History;
import com.lhj.fitnessbooking.domain.history.repository.HistoryRepository;
import com.lhj.fitnessbooking.domain.instructor.domain.Instructor;
import com.lhj.fitnessbooking.domain.instructor.repository.InstructorRepository;
import com.lhj.fitnessbooking.domain.member.domain.Member;
import com.lhj.fitnessbooking.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.lhj.fitnessbooking.domain.course.domain.DayOfWeek.*;
import static com.lhj.fitnessbooking.domain.history.domain.CourseStatus.*;
import static com.lhj.fitnessbooking.domain.member.domain.MemberGrade.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HistoryRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired InstructorRepository instructorRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired HistoryRepository historyRepository;
    @Autowired CourseHistoryRepository courseHistoryRepository;

    @AfterEach
    void afterEach() {
        historyRepository.deleteAll();
    }

    @DisplayName("홈 > 그룹예약: 수강 및 예약 날짜 조회(1)")
    @Test
    void getHistoryDateTest1() {

        // given
        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));

        Instructor instructor = new Instructor("지수");
        instructorRepository.save(instructor);

        Course course = new Course(instructor, "캐딜락", MON, LocalTime.of(9, 0), LocalTime.of(9, 50));
        courseRepository.save(course);

        int year = 2025;
        int month = 1;

        historyRepository.save(new History(member, LocalDate.of(year, month, 26), course, year, month, LocalDateTime.of(2025, 1, 26, 8, 0), CANCELED));
        historyRepository.save(new History(member, LocalDate.of(year, month, 26), course, year, month, LocalDateTime.of(2025, 1, 26, 9, 0), RESERVED));

        // when
        List<History> historyList = historyRepository.getHistoryDate(member, year, month);

        // then
        assertThat(historyList).hasSize(1);
    }

    @DisplayName("홈 > 그룹예약: 수강 및 예약 날짜 조회(2)")
    @Test
    void getHistoryDateTest2() {

        // given
        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));

        Instructor instructor = new Instructor("지수");
        instructorRepository.save(instructor);

        Course course = new Course(instructor, "캐딜락", MON, LocalTime.of(9, 0), LocalTime.of(9, 50));
        courseRepository.save(course);

        int year = 2025;
        int month = 1;

        historyRepository.save(new History(member, LocalDate.of(year, month, 26), course, year, month, LocalDateTime.of(2025, 1, 26, 8, 0), ENROLLED));
        historyRepository.save(new History(member, LocalDate.of(year, month, 26), course, year, month, LocalDateTime.of(2025, 1, 26, 9, 0), RESERVED));

        // when
        List<History> historyList = historyRepository.getHistoryDate(member, year, month);

        // then
        assertThat(historyList).hasSize(2);
    }

    @DisplayName("홈 > 그룹예약: 수강 및 예약 날짜 조회(3)")
    @Test
    public void getHistoryDateTest3() {

        // given
        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));

        Instructor instructor = new Instructor("지수");
        instructorRepository.save(instructor);

        Course course = new Course(instructor, "캐딜락", MON, LocalTime.of(9, 0), LocalTime.of(9, 50));
        courseRepository.save(course);

        int year = 2025;
        int month = 1;

        historyRepository.save(new History(member, LocalDate.of(year, month, 26), course, year, month, LocalDateTime.of(2025, 1, 26, 8, 0), RESERVED));
        historyRepository.save(new History(member, LocalDate.of(year, month, 26), course, year, month, LocalDateTime.of(2025, 1, 26, 9, 0), CANCELED));

        // when
        List<History> historyList = historyRepository.getHistoryDate(member, year, month);

        // then
        assertThat(historyList).hasSize(1);
    }

    @DisplayName("홈 > 그룹예약: 수강 및 예약 날짜 조회(4)")
    @Test
    public void getHistoryDateTest4() {

        // given
        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));

        Instructor instructor = new Instructor("지수");
        instructorRepository.save(instructor);

        Course course = new Course(instructor, "캐딜락", MON, LocalTime.of(9, 0), LocalTime.of(9, 50));
        courseRepository.save(course);

        int year = 2025;
        int month = 1;

        historyRepository.save(new History(member, LocalDate.of(year, month, 26), course, year, month, LocalDateTime.of(2025, 1, 26, 8, 0), RESERVED));
        historyRepository.save(new History(member, LocalDate.of(year, month, 26), course, year, month, LocalDateTime.of(2025, 1, 26, 9, 0), ENROLLED));

        // when
        List<History> historyList = historyRepository.getHistoryDate(member, year, month);

        // then
        assertThat(historyList).hasSize(2);
    }

    @DisplayName("홈 > 그룹예약: 수강 및 예약 날짜 조회(5)")
    @Test
    public void getHistoryDateTest5() {

        // given
        Member member1 = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));
        Member member2 = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));

        Instructor instructor = new Instructor("지수");
        instructorRepository.save(instructor);

        Course course = new Course(instructor, "캐딜락", MON, LocalTime.of(9, 0), LocalTime.of(9, 50));
        courseRepository.save(course);

        int year = 2025;
        int month = 1;

        historyRepository.save(new History(member1, LocalDate.of(year, month, 26), course, year, month, LocalDateTime.of(2025, 1, 26, 8, 0), RESERVED));
        historyRepository.save(new History(member2, LocalDate.of(year, month, 26), course, year, month, LocalDateTime.of(2025, 1, 26, 9, 0), CANCELED));
        historyRepository.save(new History(member2, LocalDate.of(year, month, 26), course, year, month, LocalDateTime.of(2025, 1, 26, 9, 0), RESERVED));

        // when
        List<History> historyList = historyRepository.getHistoryDate(member1, year, month);

        // then
        assertThat(historyList).hasSize(1);
    }

    @DisplayName("수강 기록 조회")
    @Test
    void getHistory() {

        // given
        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));
        Course course = saveCourse("지수", "캐딜락", MON, LocalTime.of(9, 0), LocalTime.of(9, 50));
        saveCourseHistory(course, LocalDate.of(2025, 1, 30), 4);
        saveCourseHistory(course, LocalDate.of(2025, 1, 31), 5);
        saveCourseHistory(course, LocalDate.of(2025, 2, 3), 5);
        saveCourseHistory(course, LocalDate.of(2025, 2, 4), 5);
        saveCourseHistory(course, LocalDate.of(2025, 2, 5), 5);
        saveCourseHistory(course, LocalDate.of(2025, 2, 6), 5);

        saveHistory(member, LocalDate.of(2025, 1, 30), course, 2025, 1, LocalDateTime.of(2025, 1, 30, 12, 35), ENROLLED);
        saveHistory(member, LocalDate.of(2025, 1, 31), course, 2025, 1, LocalDateTime.of(2025, 1, 31, 12, 35), ENROLLED);
        saveHistory(member, LocalDate.of(2025, 2, 3), course, 2025, 2, LocalDateTime.of(2025, 2, 3, 12, 35), RESERVED);
        saveHistory(member, LocalDate.of(2025, 2, 4), course, 2025, 2, LocalDateTime.of(2025, 2, 4, 12, 35), CANCELED);
        saveHistory(member, LocalDate.of(2025, 2, 6), course, 2025, 2, LocalDateTime.of(2025, 4, 5, 12, 35), RESERVED);
        saveHistory(member, LocalDate.of(2025, 2, 6), course, 2025, 2, LocalDateTime.of(2025, 4, 6, 12, 35), ENROLLED);

        // when
        List<CourseHistoryTmp> historyTmpList = historyRepository.getHistory(member, 2025, 2);

        // then
        assertThat(historyTmpList).hasSize(2);
    }

    @DisplayName("수강 취소 횟수 구하기")
    @Test
    void getCancelCount() {

        // given
        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2025, 1, 31));
        Course course1 = saveCourse("지수", "캐딜락", TUES, LocalTime.of(18, 0), LocalTime.of(18, 50));
        Course course2 = saveCourse("지수", "바렐", TUES, LocalTime.of(20, 0), LocalTime.of(20, 50));

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
        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2025, 1, 31));

        Course course = saveCourse("지수", "캐딜락", THUR, LocalTime.of(18, 0), LocalTime.of(18, 50));
        saveHistory(member, LocalDate.of(2025, 1, 31), course, 2025, 1, LocalDateTime.of(2025, 1, 30, 8, 0), RESERVED);
        saveCourseHistory(course, LocalDate.of(2025, 1, 31), 5);

        // when, when
        historyRepository.ifAfter4hour(member, LocalDate.of(2025, 2, 27), course, LocalTime.of(13, 59, 59))
                .isEmpty();
    }

    @DisplayName("수업 시작 4시간 전 취소 - 실패 케이스")
    @Test
    void ifBefore4hourFail() {

        // given
        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2025, 1, 31));

        Course course = saveCourse("지수", "캐딜락", TUES, LocalTime.of(18, 0), LocalTime.of(18, 50));
        saveHistory(member, LocalDate.of(2025, 1, 31), course, 2025, 1, LocalDateTime.of(2025, 1, 30, 8, 0), RESERVED);
        saveCourseHistory(course, LocalDate.of(2025, 1, 31), 5);

        // when, then
        historyRepository.ifAfter4hour(member, LocalDate.of(2025, 2, 27), course, LocalTime.of(14, 0, 0))
                .isPresent();
    }

    @DisplayName("특정 날짜에서 ENROLLED 또는 (최종 상태가) RESERVED인 레코드 개수 구하기")
    @Test
    void getReservedAndEnrolled() {

        // given
        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2025, 1, 31));
        Course course = saveCourse("지수", "캐딜락", TUES, LocalTime.of(18, 0), LocalTime.of(18, 50));

        historyRepository.save(new History(member, LocalDate.of(2025, 2, 26), course, 2025, 2, LocalDateTime.now(), ENROLLED));
        historyRepository.save(new History(member, LocalDate.of(2025, 2, 26), course, 2025, 2, LocalDateTime.now(), RESERVED));
        historyRepository.save(new History(member, LocalDate.of(2025, 2, 26), course, 2025, 2, LocalDateTime.now(), CANCELED));
        historyRepository.save(new History(member, LocalDate.of(2025, 2, 26), course, 2025, 2, LocalDateTime.now(), RESERVED));

        // when
        List<History> reservedAndEnrolled = historyRepository.getReservedAndEnrolled(LocalDate.of(2025, 2, 26));

        // then
        assertThat(reservedAndEnrolled).hasSize(2);
    }
    
    @DisplayName("특정 날짜, 특정 수업에 대해 이미 신청했는지(최종 상태가 RESERVED인 history가 존재하는지) 확인하기 - 존재하지 않는 경우")
    @Test
    void checkAlreadyRegistered1() {
        
        // given
        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2025, 1, 31));
        Course course = saveCourse("지수", "캐딜락", TUES, LocalTime.of(18, 0), LocalTime.of(18, 50));

        historyRepository.save(new History(member, LocalDate.of(2025, 2, 26), course, 2025, 2, LocalDateTime.now(), RESERVED));
        historyRepository.save(new History(member, LocalDate.of(2025, 2, 26), course, 2025, 2, LocalDateTime.now(), CANCELED));
        
        // when, then
        historyRepository.checkAlreadyRegistered(LocalDate.of(2025, 2, 26), course)
                .isEmpty();
    }

    @DisplayName("특정 날짜, 특정 수업에 대해 이미 신청했는지(최종 상태가 RESERVED인 history가 존재하는지) 확인하기 - 존재하는 경우")
    @Test
    void checkAlreadyRegistered2() {

        // given
        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2025, 1, 31));
        Course course = saveCourse("지수", "캐딜락", TUES, LocalTime.of(18, 0), LocalTime.of(18, 50));

        historyRepository.save(new History(member, LocalDate.of(2025, 2, 26), course, 2025, 2, LocalDateTime.now(), RESERVED));
        historyRepository.save(new History(member, LocalDate.of(2025, 2, 26), course, 2025, 2, LocalDateTime.now(), CANCELED));
        historyRepository.save(new History(member, LocalDate.of(2025, 2, 26), course, 2025, 2, LocalDateTime.now(), RESERVED));

        // when, then
        historyRepository.checkAlreadyRegistered(LocalDate.of(2025, 2, 26), course)
                .isPresent();
    }

    private Member saveMember(String memberNum, String password, String name, String phone, boolean gender, LocalDate regDate) {

        Member member = new Member(memberNum, password, name, phone, gender, MEMBER, regDate);
        memberRepository.save(member);
        return member;
    }

    private Course saveCourse(String instructorName, String courseName, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {

        Instructor instructor = new Instructor(instructorName);
        instructorRepository.save(instructor);

        Course course = new Course(instructor, courseName, dayOfWeek, startTime, endTime);
        courseRepository.save(course);
        return course;
    }

    private History saveHistory(Member member, LocalDate courseDate, Course course, int year, int month, LocalDateTime regDateTime, CourseStatus status) {

        History history = new History(member, courseDate, course, year, month, regDateTime, status);
        historyRepository.save(history);
        return history;
    }

    private CourseHistory saveCourseHistory(Course course, LocalDate date, int count) {

        CourseHistory courseHistory = new CourseHistory(course, date, count);
        courseHistoryRepository.save(courseHistory);
        return courseHistory;
    }
}