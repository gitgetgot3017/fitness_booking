package com.lhj.FitnessBooking.member;

import com.lhj.FitnessBooking.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @DisplayName("회원번호로 회원 찾기: 성공 케이스")
    @Test
    void findByMemberNumSuccess() {

        // given
        Member member = new Member("2073", "060820", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));
        memberRepository.save(member);

        // when, then
        assertThat(memberRepository.findByMemberNum("2073")).isNotEmpty();
    }

    @DisplayName("회원번호로 회원 찾기: 실패 케이스")
    @Test
    void findByMemberNumFail() {

        // given
        Member member = new Member("2074", "060820", "이현지", "01062802073", false, LocalDate.of(2024, 6, 18));
        memberRepository.save(member);

        // when, then
        assertThat(memberRepository.findByMemberNum("2073")).isEmpty();

    }
}