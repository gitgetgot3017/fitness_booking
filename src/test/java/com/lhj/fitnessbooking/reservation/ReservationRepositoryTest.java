package com.lhj.fitnessbooking.reservation;

import com.lhj.fitnessbooking.domain.course.domain.DayOfWeek;
import com.lhj.fitnessbooking.course.CourseRepository;
import com.lhj.fitnessbooking.domain.course.domain.Course;
import com.lhj.fitnessbooking.domain.instructor.domain.Instructor;
import com.lhj.fitnessbooking.domain.member.domain.Member;
import com.lhj.fitnessbooking.domain.reservation.domain.Reservation;
import com.lhj.fitnessbooking.domain.reservation.repository.ReservationRepository;
import com.lhj.fitnessbooking.domain.instructor.repository.InstructorRepository;
import com.lhj.fitnessbooking.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.lhj.fitnessbooking.domain.course.domain.DayOfWeek.TUES;
import static com.lhj.fitnessbooking.domain.member.domain.MemberGrade.*;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class ReservationRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired InstructorRepository instructorRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired
    ReservationRepository reservationRepository;

    @DisplayName("대기 인원 구하기")
    @Test
    void findByCourseDateAndCourse() {

        // given
        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2025, 1, 31));
        Course course = saveCourse("캐딜락", TUES, LocalTime.of(18, 0), LocalTime.of(0, 0));

        reservationRepository.save(new Reservation(LocalDate.of(2025, 2, 3), course, member, LocalDateTime.of(2025, 2, 3, 23, 12)));
        reservationRepository.save(new Reservation(LocalDate.of(2025, 2, 3), course, member, LocalDateTime.of(2025, 2, 3, 23, 12)));
        reservationRepository.save(new Reservation(LocalDate.of(2025, 2, 3), course, member, LocalDateTime.of(2025, 2, 3, 23, 12)));
        reservationRepository.save(new Reservation(LocalDate.of(2025, 2, 3), course, member, LocalDateTime.of(2025, 2, 3, 23, 12)));
        reservationRepository.save(new Reservation(LocalDate.of(2025, 2, 3), course, member, LocalDateTime.of(2025, 2, 3, 23, 12)));
        reservationRepository.save(new Reservation(LocalDate.of(2025, 2, 3), course, member, LocalDateTime.of(2025, 2, 3, 23, 12)));

        // when
        List<Reservation> reservations = reservationRepository.findByCourseDateAndCourse(LocalDate.of(2025, 2, 3), course);

        // then
        assertThat(reservations).hasSize(6);
    }

    @DisplayName("이미 대기 등록한 수업인지 확인하기 - 이미 대기 등록한 경우")
    @Test
    void findByCourseDateAndCourseAndMember1() {

        // given
        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2025, 1, 31));
        Course course = saveCourse("캐딜락", TUES, LocalTime.of(18, 0), LocalTime.of(0, 0));

        reservationRepository.save(new Reservation(LocalDate.of(2025, 2, 27), course, member, LocalDateTime.of(2025, 2, 3, 23, 12)));

        // when, then
        reservationRepository.findByCourseDateAndCourseAndMember(LocalDate.of(2025, 2, 27), course, member)
                .isPresent();
    }

    @DisplayName("이미 대기 등록한 수업인지 확인하기 - 대기 등록하지 않은 경우")
    @Test
    void findByCourseDateAndCourseAndMember2() {

        // given
        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2025, 1, 31));
        Course course = saveCourse("캐딜락", TUES, LocalTime.of(18, 0), LocalTime.of(0, 0));

        // when, then
        reservationRepository.findByCourseDateAndCourseAndMember(LocalDate.of(2025, 2, 27), course, member)
                .isEmpty();
    }

    @DisplayName("특정 수업의 대기 전부 삭제하기")
    @Test
    void deleteReservations() {

        // given
        Member member1 = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2025, 1, 31));
        Member member2 = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2025, 1, 31));
        Member member3 = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2025, 1, 31));
        Course course = saveCourse("캐딜락", TUES, LocalTime.of(18, 0), LocalTime.of(0, 0));

        reservationRepository.save(new Reservation(LocalDate.of(2025, 2, 4), course, member1, LocalDateTime.of(2025, 2, 4, 22, 18)));
        reservationRepository.save(new Reservation(LocalDate.of(2025, 2, 4), course, member2, LocalDateTime.of(2025, 2, 4, 22, 18)));
        reservationRepository.save(new Reservation(LocalDate.of(2025, 2, 4), course, member3, LocalDateTime.of(2025, 2, 4, 22, 18)));

        // when
        reservationRepository.deleteReservations(LocalDate.of(2025, 2, 4), course);

        // then
        List<Reservation> reservations = reservationRepository.findByCourseDateAndCourse(LocalDate.of(2025, 2, 4), course);
        assertThat(reservations).isEmpty();
    }

    @DisplayName("특정 수업에 대한 특정 회원의 대기 삭제하기")
    @Test
    void deleteReservation() {

        // given
        Member member1 = saveMember("1073", "060820", "일현지", "01052802073", true, LocalDate.of(2025, 1, 31));
        Member member2 = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2025, 1, 31));
        Course course = saveCourse("캐딜락", TUES, LocalTime.of(18, 0), LocalTime.of(0, 0));

        reservationRepository.save(new Reservation(LocalDate.of(2025, 2, 4), course, member1, LocalDateTime.of(2025, 2, 4, 22, 18)));
        reservationRepository.save(new Reservation(LocalDate.of(2025, 2, 4), course, member2, LocalDateTime.of(2025, 2, 4, 22, 18)));

        // when
        reservationRepository.deleteReservation(LocalDate.of(2025, 2, 4), course, member1);

        // then
        List<Reservation> reservations = reservationRepository.findByCourseDateAndCourse(LocalDate.of(2025, 2, 4), course);
        assertThat(reservations).hasSize(1);
        assertThat(reservations.get(0).getMember()).isEqualTo(member2);
    }

    private Member saveMember(String memberNum, String password, String name, String phone, boolean gender, LocalDate regDate) {

        Member member = new Member(memberNum, password, name, phone, gender, MEMBER, regDate);
        memberRepository.save(member);
        return member;
    }

    private Course saveCourse(String name, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {

        Instructor instructor = new Instructor(name);
        instructorRepository.save(instructor);

        Course course = new Course(instructor, name, dayOfWeek, startTime, endTime);
        courseRepository.save(course);
        return course;
    }
}