package com.lhj.fitnessbooking.global.auth.controller;

import com.lhj.fitnessbooking.global.auth.dto.Jwt;
import com.lhj.fitnessbooking.global.auth.dto.RefreshTokenRequest;
import com.lhj.fitnessbooking.global.auth.exception.InvalidRefreshTokenException;
import com.lhj.fitnessbooking.global.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtController {

    private final JwtService jwtService;

    /**
     * access token 재발급 요청
     */
    @PatchMapping("/api/refresh/token")
    public HttpEntity<Jwt> refreshTokens(@RequestBody RefreshTokenRequest refreshTokenRequest) {

        try {
            Jwt jwt = jwtService.refreshTokens(refreshTokenRequest.getAccessToken(), refreshTokenRequest.getRefreshToken());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (InvalidRefreshTokenException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }
    }
}
