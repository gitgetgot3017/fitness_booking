package com.lhj.fitnessbooking.global.auth.service;

import com.lhj.fitnessbooking.domain.member.domain.Member;
import com.lhj.fitnessbooking.domain.member.exception.NotExistMemberException;
import com.lhj.fitnessbooking.domain.member.repository.MemberRepository;
import com.lhj.fitnessbooking.global.auth.dto.Jwt;
import com.lhj.fitnessbooking.global.auth.exception.InvalidRefreshTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final MemberRepository memberRepository;

    @Value("${jwt.secret}")
    private String secretKeyString;

    private byte[] secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        secretKey = secretKeyString.getBytes();
        key = Keys.hmacShaKeyFor(secretKey);
    }

    private static final long accessTokenExpiration = 1000 * 60 * 15; // 15분
    private static final long refreshTokenExpiration = 1000 * 60 * 60 * 24 * 14; // 2주

    public Jwt refreshTokens(String accessToken, String refreshToken) {

        // 사용자가 전달한 refresh token의 유효성 검증(null, 만료/위변조/포맷 오류 등)
        try {
            getClaims(refreshToken);
        } catch (IllegalArgumentException | JwtException e) {
            throw new InvalidRefreshTokenException("refresh token이 유효하지 않습니다.");
        }

        // access token에서 claims(memberNum) 추출
        Claims claims;
        try {
            getClaims(accessToken);
            throw new IllegalArgumentException("access token이 만료되지 않았습니다");
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }

        String memberNum = claims.get("memberNum", String.class);
        Member member = memberRepository.findByMemberNum(memberNum)
                .orElseThrow(() -> new NotExistMemberException("해당 회원은 존재하지 않습니다."));

        // Redis에서 refresh token 조회 및 사용자가 전달한 것과 비교
//        String redisRefreshToken = stringRedisTemplate.opsForValue().get("RT:" + member.getId());
//        if (redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)) {
//            throw new InvalidRefreshTokenException("refresh token이 유효하지 않습니다.");
//        }

        // 토큰 재발급
        return createJwt(makeClaims(member), false);
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Map<String, String> makeClaims(Member member) {
        Map<String, String> claims = new HashMap<>();
        claims.put("memberNum", member.getMemberNum());
        claims.put("role", "member");
        return claims;
    }

    public Jwt createJwt(Map<String, String> claims, boolean issueRefreshToken) {

        String accessToken = createToken(claims, new Date(System.currentTimeMillis() + accessTokenExpiration));
        if (issueRefreshToken == true) {
            String refreshToken = createToken(new HashMap<>(), new Date(System.currentTimeMillis() + refreshTokenExpiration));
            return new Jwt(accessToken, refreshToken);
        }
        return new Jwt(accessToken, null);
    }

    private String createToken(Map<String, String> claims, Date expirationDate) {

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }
}
