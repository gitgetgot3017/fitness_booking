package com.lhj.FitnessBooking.course;

import com.lhj.FitnessBooking.domain.Course;
import com.lhj.FitnessBooking.domain.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("select c from Course c where c.dayOfWeek = :dayOfWeek and :startTime < c.startTime")
    List<Course> getCourses(@Param("dayOfWeek") DayOfWeek dayOfWeek, @Param("startTime") LocalTime startTime);
}
