package com.lhj.FitnessBooking.subscription;

import com.lhj.FitnessBooking.domain.Member;
import com.lhj.FitnessBooking.domain.MemberGrade;
import com.lhj.FitnessBooking.domain.Subscription;
import com.lhj.FitnessBooking.dto.CourseMainHeader;
import com.lhj.FitnessBooking.member.MemberRepository;
import com.lhj.FitnessBooking.subscription.exception.NotExistSubscriptionException;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.lhj.FitnessBooking.domain.MemberGrade.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class SubscriptionRepositoryTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private SubscriptionRepository subscriptionRepository;

    @Autowired EntityManager em;

    @DisplayName("홈 > 그룹예약: 회원 및 이용권 정보 조회")
    @Test
    void getSubscription() {

        // given
        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));

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

    @DisplayName("예약 횟수 증가시키기")
    @Test
    void increaseReservedCount() {

        // given
        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));
        saveSubscription(member, LocalDate.of(2024, 6, 18), LocalDate.of(2025, 2, 23), 1, 75, 77);

        CourseMainHeader beforeSubscription = subscriptionRepository.getSubscription(member, LocalDate.of(2025, 2, 4));
        int reservedCount = beforeSubscription.getReservedCount();

        // when
        subscriptionRepository.increaseReservedCount(member);

        // then
        CourseMainHeader afterSubscription = subscriptionRepository.getSubscription(member, LocalDate.of(2025, 2, 4));
        assertThat(afterSubscription.getReservedCount()).isEqualTo(reservedCount + 1);
    }

    @DisplayName("예약 횟수 감소시키기")
    @Test
    void decreaseReservedCount() {

        // given
        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));
        saveSubscription(member, LocalDate.of(2024, 6, 18), LocalDate.of(2025, 2, 23), 1, 75, 77);

        CourseMainHeader beforeSubscription = subscriptionRepository.getSubscription(member, LocalDate.of(2025, 2, 4));
        int reservedCount = beforeSubscription.getReservedCount();

        // when
        subscriptionRepository.decreaseReservedCount(member);

        // then
        CourseMainHeader afterSubscription = subscriptionRepository.getSubscription(member, LocalDate.of(2025, 2, 4));
        assertThat(afterSubscription.getReservedCount()).isEqualTo(reservedCount - 1);
    }

    @DisplayName("수업 시작 시, completed_count는 1 증가시키고 reserved_count는 1 감소시킨다.")
    @Test
    void changeCount() {

        // given
        Member member = saveMember("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));

        saveSubscription(member, LocalDate.of(2024, 6, 18), LocalDate.of(2025, 2, 12), 0, 77, 77); // 만료된 이용권
        Subscription subscription = saveSubscription(member, LocalDate.of(2024, 6, 18), LocalDate.of(2025, 2, 23), 1, 75, 77); // 만료되어 가는 이용권
        saveSubscription(member, LocalDate.of(2025, 2, 12), LocalDate.of(2025, 5, 11), 0, 0, 77); // 새로 발급한 이용권
        em.clear();

        // when
        subscriptionRepository.changeCount(member);

        // then
        subscription = subscriptionRepository.findById(subscription.getId())
                .orElseThrow(() -> new NotExistSubscriptionException("해당 이용권은 존재하지 않습니다."));
        assertThat(subscription.getCompletedCount()).isEqualTo(76);
    }

    private Member saveMember(String memberNum, String password, String name, String phone, boolean gender, LocalDate regDate) {

        Member member = new Member(memberNum, password, name, phone, gender, MEMBER, regDate);
        memberRepository.save(member);
        return member;
    }

    private Subscription saveSubscription(Member member, LocalDate startDate, LocalDate endDate, int reservedCount, int completedCount, int availableCount) {

        Subscription subscription = new Subscription(member, startDate, endDate, reservedCount, completedCount, availableCount);
        subscriptionRepository.save(subscription);
        return subscription;
    }
}