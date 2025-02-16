package com.lhj.FitnessBooking.member;

import com.lhj.FitnessBooking.domain.Member;
import com.lhj.FitnessBooking.domain.MemberGrade;
import com.lhj.FitnessBooking.jwt.JwtService;
import com.lhj.FitnessBooking.member.dto.LoginResponse;
import com.lhj.FitnessBooking.member.dto.LoginRequest;
import com.lhj.FitnessBooking.member.exception.LoginFailException;
import com.lhj.FitnessBooking.jwt.dto.Jwt;
import com.lhj.FitnessBooking.member.exception.NotExistMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.lhj.FitnessBooking.domain.MemberGrade.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest joinRequest) {

        Member member = memberRepository.findByMemberNum(joinRequest.getMemberNum())
                .orElseThrow(() -> new LoginFailException("해당 아이디가 존재하지 않습니다."));
        if (!joinRequest.getPassword().equals(member.getPassword())) {
            throw new LoginFailException("비밀번호를 잘못 입력하셨습니다.");
        }

        // 아이디와 비밀번호를 제대로 입력한 경우(로그인에 성공한 경우)
        Jwt jwt = jwtService.createJwt(makeClaims(joinRequest));
        member.updateRefreshToken(jwt.getRefreshToken());
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
