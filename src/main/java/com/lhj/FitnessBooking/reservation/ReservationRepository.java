package com.lhj.FitnessBooking.reservation;

import com.lhj.FitnessBooking.domain.Course;
import com.lhj.FitnessBooking.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByCourseDateAndCourse(LocalDate courseDate, Course course);
}
