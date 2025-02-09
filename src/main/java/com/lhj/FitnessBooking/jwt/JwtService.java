package com.lhj.FitnessBooking.jwt;

import com.lhj.FitnessBooking.domain.Member;
import com.lhj.FitnessBooking.jwt.dto.Jwt;
import com.lhj.FitnessBooking.jwt.exception.NotExistRefreshTokenException;
import com.lhj.FitnessBooking.member.MemberRepository;
import com.lhj.FitnessBooking.member.dto.LoginRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final MemberRepository memberRepository;

    public byte[] secretKey = "IzNiO1zSXRe8l1pRFckh/x8cKcNtILKJlGCpGMzaCF8=".getBytes(); // TODO: application.yml에 적기
    private Key key = Keys.hmacShaKeyFor(secretKey);

    private static final long accessTokenExpiration = 1000 * 60 * 15; // 15분
    private static final long refreshTokenExpiration = 1000 * 60 * 60 * 24 * 14; // 2주

    public Jwt createJwt(Map<String, String> claims) {

        String accessToken = createToken(claims, new Date(System.currentTimeMillis() + accessTokenExpiration));
        String refreshToken = createToken(new HashMap<>(), new Date(System.currentTimeMillis() + refreshTokenExpiration));
        return new Jwt(accessToken, refreshToken);
    }

    private String createToken(Map<String, String> claims, Date expirationDate) {

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Jwt refreshTokens(String refreshToken) {

        try {
            getClaims(refreshToken);

            Member member = memberRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new NotExistRefreshTokenException("refresh token이 존재하지 않습니다."));

            Jwt jwt = createJwt(makeClaims(member));
            member.updateRefreshToken(jwt.getRefreshToken());
            return jwt;
        } catch (RuntimeException e) {
            return null;
        }
    }

    private Map<String, String> makeClaims(Member member) {
        Map<String, String> claims = new HashMap<>();
        claims.put("memberNum", member.getMemberNum());
        claims.put("role", "member");
        return claims;
    }
}
