package com.lhj.fitnessbooking.global.auth.service;

import com.lhj.fitnessbooking.domain.member.domain.Member;
import com.lhj.fitnessbooking.global.auth.dto.Jwt;
import com.lhj.fitnessbooking.global.auth.exception.NotExistRefreshTokenException;
import com.lhj.fitnessbooking.domain.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
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
