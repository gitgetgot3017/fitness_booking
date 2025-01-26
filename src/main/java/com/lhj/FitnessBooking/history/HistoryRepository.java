package com.lhj.FitnessBooking.history;

import com.lhj.FitnessBooking.domain.CourseStatus;
import com.lhj.FitnessBooking.domain.History;
import com.lhj.FitnessBooking.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

    @Query(
            "select h " +
            "from History h " +
            "where h.member = :member " +
            "and h.year = :year and h.month = :month " +
            "and (h.status = :status1 or (h.status = :status2 and h.id in (select max(subH.id) from History subH where subH.status <> :status1 group by subH.course)))"
    )
    List<History> getHistory(@Param("member") Member member,
                             @Param("year") int year,
                             @Param("month") int month,
                             @Param("status1") CourseStatus status1, // ENROLLED
                             @Param("status2") CourseStatus status2); // RESERVED
}