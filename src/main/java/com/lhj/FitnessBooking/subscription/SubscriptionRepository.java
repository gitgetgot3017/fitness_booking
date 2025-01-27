package com.lhj.FitnessBooking.subscription;

import com.lhj.FitnessBooking.domain.Member;
import com.lhj.FitnessBooking.domain.Subscription;
import com.lhj.FitnessBooking.dto.CourseMainHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("select new com.lhj.FitnessBooking.dto.CourseMainHeader(m.name, m.memberNum, s.name, min(s.startDate), s.endDate, s.reservedCount, s.completedCount, s.availableCount) " +
            "from Member m join Subscription s on m = s.member " +
            "where s.member = :member and :curDate < s.endDate and s.completedCount < s.availableCount order by s.startDate")
    CourseMainHeader getSubscription(@Param("member") Member member, @Param("curDate") LocalDate curDate);
}
