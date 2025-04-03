package com.lhj.FitnessBooking.instructor;

import com.lhj.FitnessBooking.domain.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    // 회원의 이름은 중복될 수 있으나 강사의 이름은 중복될 수 없도록 센터 차원에서 조치할 것임.
    Optional<Instructor> findByName(String name);
}
