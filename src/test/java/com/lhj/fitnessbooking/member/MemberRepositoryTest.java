package com.lhj.fitnessbooking.member;

import com.lhj.fitnessbooking.domain.member.repository.MemberRepository;
import com.lhj.fitnessbooking.domain.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static com.lhj.fitnessbooking.domain.member.domain.MemberGrade.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @DisplayName("회원번호로 회원 찾기: 성공 케이스")
    @Test
    void findByMemberNumSuccess() {

        // given
        Member member = new Member("2073", "060820", "이현지", "01062802073", false, MEMBER, LocalDate.of(2024, 6, 18));
        memberRepository.save(member);

        // when, then
        assertThat(memberRepository.findByMemberNum("2073")).isNotEmpty();
    }

    @DisplayName("회원번호로 회원 찾기: 실패 케이스")
    @Test
    void findByMemberNumFail() {

        // given
        Member member = new Member("2074", "060820", "이현지", "01062802073", false, MEMBER, LocalDate.of(2024, 6, 18));
        memberRepository.save(member);

        // when, then
        assertThat(memberRepository.findByMemberNum("2073")).isEmpty();

    }

    @DisplayName("refresh token으로 회원 찾기")
    @Test
    void findByRefreshToken() {

        // give
        String refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MzkxMTQ5NTUsImV4cCI6MTc0MDMyNDU1NX0.g4HvQ0qCC9QR3lsUctRuZFzvTWf53HGrKQLCVzNYqng";

        // when, then
        memberRepository.findByRefreshToken(refreshToken)
                .isPresent();
    }
}