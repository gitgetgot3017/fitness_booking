package com.lhj.FitnessBooking.course;

import com.lhj.FitnessBooking.domain.Course;
import com.lhj.FitnessBooking.dto.CourseInfoTmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("select new com.lhj.FitnessBooking.dto.CourseInfoTmp(i.name, c.name, c.startTime, ch.count) " +
            "from Instructor i join Course c on i = c.instructor join CourseHistory ch on c = ch.course " +
            "where ch.date = :date and :startTime < c.startTime")
    List<CourseInfoTmp> getTodayCourses(@Param("date") LocalDate date, @Param("startTime") LocalTime startTime);

    @Query("select new com.lhj.FitnessBooking.dto.CourseInfoTmp(i.name, c.name, c.startTime, ch.count) " +
            "from Instructor i join Course c on i = c.instructor join CourseHistory ch on c = ch.course " +
            "where ch.date = :date")
    List<CourseInfoTmp> getTomorrowCourses(@Param("date") LocalDate date);
}
