package com.lhj.FitnessBooking.courseHistory;

import com.lhj.FitnessBooking.domain.Course;
import com.lhj.FitnessBooking.domain.CourseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CourseHistoryRepository extends JpaRepository<CourseHistory, Long> {

    List<CourseHistory> findByDate(LocalDate date);

    Optional<CourseHistory> findByDateAndCourse(LocalDate date, Course course);
}
