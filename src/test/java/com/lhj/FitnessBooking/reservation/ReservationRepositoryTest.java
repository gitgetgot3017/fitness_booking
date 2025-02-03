package com.lhj.FitnessBooking.reservation;

import com.lhj.FitnessBooking.course.CourseRepository;
import com.lhj.FitnessBooking.domain.*;
import com.lhj.FitnessBooking.instructor.InstructorRepository;
import com.lhj.FitnessBooking.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.lhj.FitnessBooking.domain.DayOfWeek.TUES;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ReservationRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired InstructorRepository instructorRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired ReservationRepository reservationRepository;

    @DisplayName("")
    @Test
    void findByCourseDateAndCourse() {

        // given
        Member member = saveMember("2073", "이현지", "01062802073", false, LocalDate.of(2025, 1, 31));
        Course course = saveCourse("캐딜락", TUES, LocalTime.of(18, 0));

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
}