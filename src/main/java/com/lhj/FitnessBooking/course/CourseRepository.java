package com.lhj.FitnessBooking.course;

import com.lhj.FitnessBooking.domain.Course;
import com.lhj.FitnessBooking.domain.DayOfWeek;
import com.lhj.FitnessBooking.dto.CourseInfoTmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("select new com.lhj.FitnessBooking.dto.CourseInfoTmp(i.name, c.name, c.startTime, ch.count) " +
            "from Instructor i join Course c on i = c.instructor join CourseHistory ch on c = ch.course " +
            "where ch.week = :week and c.dayOfWeek = :dayOfWeek and :startTime < c.startTime")
    List<CourseInfoTmp> getCourses(@Param("week") int week, @Param("dayOfWeek") DayOfWeek dayOfWeek, @Param("startTime") LocalTime startTime);
}
