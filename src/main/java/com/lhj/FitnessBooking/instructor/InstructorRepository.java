package com.lhj.FitnessBooking.instructor;

import com.lhj.FitnessBooking.domain.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}
