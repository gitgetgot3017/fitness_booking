package com.lhj.fitnessbooking.domain.member.repository;

import com.lhj.fitnessbooking.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberNum(String memberNum);
}
