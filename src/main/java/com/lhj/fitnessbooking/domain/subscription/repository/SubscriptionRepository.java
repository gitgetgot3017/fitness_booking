<<<<<<< Updated upstream:src/main/java/com/lhj/fitnessbooking/domain/subscription/repository/SubscriptionRepository.java
package com.lhj.fitnessbooking.domain.subscription.repository;

import com.lhj.fitnessbooking.domain.course.dto.CourseMainHeader;
import com.lhj.fitnessbooking.domain.member.domain.Member;
import com.lhj.fitnessbooking.domain.subscription.domain.Subscription;
=======
package com.lhj.fitnessbooking.subscription;

import com.lhj.fitnessbooking.domain.Member;
import com.lhj.fitnessbooking.domain.Subscription;
import com.lhj.fitnessbooking.dto.CourseMainHeader;
>>>>>>> Stashed changes:src/main/java/com/lhj/fitnessbooking/subscription/SubscriptionRepository.java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("select new com.lhj.fitnessbooking.domain.course.dto.CourseMainHeader(m.name, m.memberNum, s.name, min(s.startDate), s.endDate, s.reservedCount, s.completedCount, s.availableCount) " +
            "from Member m join Subscription s on m = s.member " +
            "where s.member = :member and :curDate < s.endDate and s.completedCount < s.availableCount order by s.startDate")
    CourseMainHeader getSubscription(@Param("member") Member member, @Param("curDate") LocalDate curDate);

    @Modifying
    @Query("update Subscription s " +
            "set s.reservedCount = s.reservedCount + 1 " +
            "where s.member = :member and s.reservedCount + s.completedCount < s.availableCount")
    void increaseReservedCount(@Param("member") Member member);

    @Modifying
    @Query("update Subscription s " +
            "set s.reservedCount = s.reservedCount - 1 " +
            "where s.member = :member and s.reservedCount + s.completedCount < s.availableCount")
    void decreaseReservedCount(@Param("member") Member member);

    @Modifying
    @Query("update Subscription s " +
            "set s.completedCount = s.completedCount + 1, " +
            "    s.reservedCount = s.reservedCount - 1 " +
            "where s.member = :member " +
            "and s.reservedCount > 0 " + // 사용 전인 이용권이 아닌 경우
            "and s.completedCount < s.availableCount") // 만료된 이용권이 아닌 경우
    void changeCount(@Param("member") Member member);
}
