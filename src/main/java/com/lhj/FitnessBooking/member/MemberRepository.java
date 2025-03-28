package com.lhj.FitnessBooking.member;

import com.lhj.FitnessBooking.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberNum(String memberNum);

    Optional<Member> findByRefreshToken(String refreshToken);
}
