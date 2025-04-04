package com.lhj.fitnessbooking.domain.member.service;

import com.lhj.fitnessbooking.domain.member.repository.MemberRepository;
import com.lhj.fitnessbooking.domain.member.dto.LoginResponse;
import com.lhj.fitnessbooking.domain.member.exception.NotExistMemberException;
import com.lhj.fitnessbooking.domain.member.domain.Member;
import com.lhj.fitnessbooking.domain.member.domain.MemberGrade;
import com.lhj.fitnessbooking.global.auth.service.JwtService;
import com.lhj.fitnessbooking.domain.member.dto.LoginRequest;
import com.lhj.fitnessbooking.domain.member.exception.LoginFailException;
import com.lhj.fitnessbooking.global.auth.dto.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static com.lhj.fitnessbooking.domain.member.domain.MemberGrade.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    @Qualifier("stringValueRedisTemplate")
    private final RedisTemplate<String, String> stringRedisTemplate;

    public LoginResponse login(LoginRequest joinRequest) {

        Member member = memberRepository.findByMemberNum(joinRequest.getMemberNum())
                .orElseThrow(() -> new LoginFailException("해당 아이디가 존재하지 않습니다."));
        if (!joinRequest.getPassword().equals(member.getPassword())) {
            throw new LoginFailException("비밀번호를 잘못 입력하셨습니다.");
        }

        // 아이디와 비밀번호를 제대로 입력한 경우(로그인에 성공한 경우)
        Jwt jwt = jwtService.createJwt(makeClaims(joinRequest), true);
        stringRedisTemplate.opsForValue().set("RT:" + member.getId(), jwt.getRefreshToken(), Duration.ofDays(14));
        return changeJwtToLoginResponse(member.getMemberNum(), jwt);
    }

    private Map<String, String> makeClaims(LoginRequest joinRequest) {
        Map<String, String> claims = new HashMap<>();
        claims.put("memberNum", joinRequest.getMemberNum());
        claims.put("role", "member");
        return claims;
    }

    private LoginResponse changeJwtToLoginResponse(String memberNum, Jwt jwt) {

        Member member = memberRepository.findByMemberNum(memberNum)
                .orElseThrow(() -> new NotExistMemberException("해당 회원은 존재하지 않습니다."));

        MemberGrade grade = MEMBER;
        if (member.getGrade() == ADMIN) {
            grade = ADMIN;
        }

        return new LoginResponse(jwt.getAccessToken(), jwt.getRefreshToken(), grade);
    }
}
