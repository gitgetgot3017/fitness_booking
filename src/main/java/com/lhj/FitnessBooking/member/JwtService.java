package com.lhj.FitnessBooking.member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    public byte[] secretKey = "IzNiO1zSXRe8l1pRFckh/x8cKcNtILKJlGCpGMzaCF8=".getBytes(); // TODO: application.yml에 적기
    private Key key = Keys.hmacShaKeyFor(secretKey);

    private static final long accessTokenExpiration = 1000 * 60 * 60 * 15; // 15분
    private static final long refreshTokenExpiration = 1000 * 60 * 60 * 60 * 24 * 14; // 2주

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
}
