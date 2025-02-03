package com.lhj.FitnessBooking.course;

import com.lhj.FitnessBooking.domain.Course;
import com.lhj.FitnessBooking.dto.CourseInfoTmp;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("select new com.lhj.FitnessBooking.dto.CourseInfoTmp(c.id, i.name, c.name, c.startTime, ch.count) " +
            "from Instructor i join Course c on i = c.instructor join CourseHistory ch on c = ch.course " +
            "where ch.date = :date and :startTime < c.startTime " +
            "order by c.startTime")
    List<CourseInfoTmp> getTodayCourses(@Param("date") LocalDate date, @Param("startTime") LocalTime startTime);

    @Query("select new com.lhj.FitnessBooking.dto.CourseInfoTmp(c.id, i.name, c.name, c.startTime, ch.count) " +
            "from Instructor i join Course c on i = c.instructor join CourseHistory ch on c = ch.course " +
            "where ch.date = :date " +
            "order by c.startTime")
    List<CourseInfoTmp> getTomorrowCourses(@Param("date") LocalDate date);

    @Query("select ch.count " +
            "from Course c join CourseHistory ch on c = ch.course where ch.date = :date and c.id = :courseId")
    int getCourseCount(@Param("date") LocalDate date, @Param("courseId") Long courseId);

    @Query("select new com.lhj.FitnessBooking.dto.CourseInfoTmp(c.id, i.name, c.name, c.startTime, ch.count) " +
            "from Instructor i join Course c on i = c.instructor join CourseHistory ch on c = ch.course " +
            "where ch.date = :date and c.id = :courseId")
    Optional<CourseInfoTmp> getCourseDetailCourseInfo(@Param("date") LocalDate date, @Param("courseId") Long courseId);

    @Modifying
    @Query("update CourseHistory ch set ch.count = ch.count + 1 where ch.date = :date and ch.course = :course")
    void increaseCourseCount(@Param("date") LocalDate date, @Param("course") Course course);

    @Modifying
    @Query("update CourseHistory ch set ch.count = ch.count - 1 where ch.date = :date and ch.course = :course")
    void decreaseCourseCount(@Param("date") LocalDate date, @Param("course") Course course);
}
