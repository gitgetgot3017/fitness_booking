package com.lhj.FitnessBooking.subscription;

import com.lhj.FitnessBooking.domain.Member;
import com.lhj.FitnessBooking.domain.Subscription;
import com.lhj.FitnessBooking.dto.CourseMainHeader;
import com.lhj.FitnessBooking.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SubscriptionRepositoryTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private SubscriptionRepository subscriptionRepository;

    @DisplayName("홈 > 그룹예약: 회원 및 이용권 정보 조회")
    @Test
    void getSubscription() {

        // given
        Member member = new Member("2073", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));
        memberRepository.save(member);

        Subscription subscription1 = new Subscription(member, LocalDate.of(2024, 6, 18), LocalDate.of(2025, 2, 23), 0, 74, 77);
        subscriptionRepository.save(subscription1);

        // 기간이 만료된 경우
        Subscription subscription2 = new Subscription(member, LocalDate.of(2024, 6, 18), LocalDate.of(2025, 1, 25), 0, 74, 77);
        subscriptionRepository.save(subscription2);

        // 횟수를 다 사용한 경우
        Subscription subscription3 = new Subscription(member, LocalDate.of(2024, 6, 18), LocalDate.of(2025, 2, 23), 0, 77, 77);
        subscriptionRepository.save(subscription3);

        // 이용권이 끝나기 전에 재등록을 한 경우
        Subscription subscription4 = new Subscription(member, LocalDate.of(2025, 2, 23), LocalDate.of(2025, 5, 23), 0, 0, 77);
        subscriptionRepository.save(subscription4);

        // when
        CourseMainHeader courseMainHeader = subscriptionRepository.getSubscription(member, LocalDate.now());

        // then
        assertThat(courseMainHeader).isNotNull();
        assertThat(courseMainHeader).extracting("memberName", "memberNum", "endDate", "completedCount")
                .containsExactly("이현지", "2073", LocalDate.of(2025, 2, 23), 74);
    }
}