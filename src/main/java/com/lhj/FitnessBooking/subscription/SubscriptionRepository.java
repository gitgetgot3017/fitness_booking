package com.lhj.FitnessBooking.subscription;

import com.lhj.FitnessBooking.domain.Member;
import com.lhj.FitnessBooking.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("select s from Subscription s where s.member = :member and :curDate < s.endDate and s.completedCount < s.availableCount order by s.startDate")
    List<Subscription> getSubscriptions(@Param("member") Member member, @Param("curDate") LocalDate curDate);
}
