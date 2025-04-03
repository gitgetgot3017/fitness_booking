package com.lhj.FitnessBooking.course;

import com.lhj.FitnessBooking.domain.Course;
import com.lhj.FitnessBooking.domain.DayOfWeek;
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

    @Query("select new com.lhj.FitnessBooking.dto.CourseInfoTmp(c.id, i.name, i.imgUrl, c.name, c.startTime, c.endTime, ch.count) " +
            "from Instructor i " +
            "join Course c on i = c.instructor " +
            "join CourseHistory ch on c = ch.course " +
            "where ch.date = :date " +
            "and :startTime < c.startTime " +
            "order by c.startTime")
    List<CourseInfoTmp> getCourses(@Param("date") LocalDate date, @Param("startTime") LocalTime startTime);

    @Query("select ch.count " +
            "from Course c join CourseHistory ch on c = ch.course where ch.date = :date and c.id = :courseId")
    int getCourseCount(@Param("date") LocalDate date, @Param("courseId") Long courseId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select ch.count " +
            "from Course c join CourseHistory ch on c = ch.course where ch.date = :date and c.id = :courseId")
    int getCourseCountWithLock(@Param("date") LocalDate date, @Param("courseId") Long courseId);

    @Query("select new com.lhj.FitnessBooking.dto.CourseInfoTmp(c.id, i.name, i.imgUrl, c.name, c.startTime, c.endTime, ch.count) " +
            "from Instructor i join Course c on i = c.instructor join CourseHistory ch on c = ch.course " +
            "where ch.date = :date and c.id = :courseId")
    Optional<CourseInfoTmp> getCourseDetailCourseInfo(@Param("date") LocalDate date, @Param("courseId") Long courseId);

    @Modifying
    @Query("update CourseHistory ch set ch.count = ch.count + 1 where ch.date = :date and ch.course = :course")
    void increaseCourseCount(@Param("date") LocalDate date, @Param("course") Course course);

    @Modifying
    @Query("update CourseHistory ch set ch.count = ch.count - 1 where ch.date = :date and ch.course = :course")
    void decreaseCourseCount(@Param("date") LocalDate date, @Param("course") Course course);

    List<Course> findByDayOfWeek(DayOfWeek dayOfWeek);

    // 하루에는 한 종류의 요가 수업이 두 번 이상 존재하지 않는다.
    Optional<Course> findByDayOfWeekAndName(DayOfWeek dayOfWeek, String name);
}
