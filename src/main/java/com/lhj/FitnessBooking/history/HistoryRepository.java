package com.lhj.FitnessBooking.history;

import com.lhj.FitnessBooking.domain.Course;
import com.lhj.FitnessBooking.domain.CourseHistory;
import com.lhj.FitnessBooking.domain.History;
import com.lhj.FitnessBooking.domain.Member;
import com.lhj.FitnessBooking.dto.CheckBefore4HourDto;
import com.lhj.FitnessBooking.dto.CourseHistoryTmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Long> {

    @Query(
            "select h " +
            "from History h " +
            "where h.member = :member " +
            "and h.year = :year and h.month = :month " +
            "and (h.status = 'ENROLLED' or (h.status = 'RESERVED' and h.id in (select max(subH.id) from History subH where subH.member = :member and subH.status <> 'ENROLLED' group by subH.course)))"
    )
    List<History> getHistoryDate(@Param("member") Member member,
                             @Param("year") int year,
                             @Param("month") int month);

    @Query(
            "select new com.lhj.FitnessBooking.dto.CourseHistoryTmp(h.courseDate, i.name, c.name, c.startTime, ch.count) " +
            "from History h " +
            "join Course c on h.course = c " +
            "join Instructor i on c.instructor = i " +
            "join CourseHistory ch on ch.date = h.courseDate " +
            "where h.member = :member " +
            "and h.year = :year and h.month = :month " +
            "and (h.status = 'ENROLLED' or (h.status = 'RESERVED' and h.id in (select max(subH.id) from History subH where subH.member = :member and subH.status <> 'ENROLLED' group by subH.course)))"
    )
    List<CourseHistoryTmp> getHistory(@Param("member") Member member,
                                      @Param("year") int year,
                                      @Param("month") int month);

    @Query("select h " +
            "from History h " +
            "where h.member = :member and function('DATE', h.regDateTime) = :date and h.status = 'CANCELED'")
    List<History> getCancelCount(@Param("member") Member member, @Param("date") LocalDate date);

    @Query("select new com.lhj.FitnessBooking.dto.CheckBefore4HourDto(c.startTime) " +
            "from History h " +
            "join Course c on h.course = c " +
            "join CourseHistory ch on c = ch.course " +
            "where h.member = :member " +
            "and ch.date = :date " +
            "and h.course = :course " +
            "and :limitTime >= c.startTime " +
            "and h.status = 'RESERVED' and h.id in (select max(subH.id) from History subH where subH.member = :member and subH.status <> 'ENROLLED' group by subH.course)")
    Optional<CheckBefore4HourDto> ifAfter4hour(@Param("member") Member member, @Param("date") LocalDate date, @Param("course") Course course, @Param("limitTime") LocalTime limitTime);

    @Query(
            "select h " +
            "from History h " +
            "where (h.status = 'ENROLLED' " +
            "or (h.status = 'RESERVED' " +
            "and h.id in (select max(h.id) " +
                            "from History h " +
                            "where h.courseDate = :courseDate " +
                            "and h.status <> 'ENROLLED' " +
                            "group by h.course))) "
    )
    List<History> getReservedAndEnrolled(@Param("courseDate") LocalDate courseDate);

    @Query(
            "select h " +
            "from History h " +
            "where h.status = 'RESERVED' " +
            "and h.id = (select max(h.id) " +
                                "from History h " +
                                "where h.courseDate = :courseDate " +
                                "and h.course = :course)"
    )
    Optional<History> checkAlreadyRegistered(@Param("courseDate") LocalDate courseDate, @Param("course") Course course);
}