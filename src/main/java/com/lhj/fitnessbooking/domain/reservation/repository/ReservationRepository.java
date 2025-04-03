package com.lhj.fitnessbooking.domain.reservation.repository;

import com.lhj.fitnessbooking.domain.course.domain.Course;
import com.lhj.fitnessbooking.domain.member.domain.Member;
import com.lhj.fitnessbooking.domain.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByCourseDateAndCourse(LocalDate courseDate, Course course);

    Optional<Reservation> findByCourseDateAndCourseAndMember(LocalDate courseDate, Course course, Member member);

    @Modifying
    @Query("delete from Reservation r where r.courseDate = :courseDate and r.course = :course")
    void deleteReservations(@Param("courseDate") LocalDate courseDate, @Param("course") Course course);

    @Modifying
    @Query("delete from Reservation r where r.courseDate = :courseDate and r.course = :course and r.member = :member")
    void deleteReservation(@Param("courseDate") LocalDate courseDate, @Param("course") Course course, @Param("member") Member member);
}
