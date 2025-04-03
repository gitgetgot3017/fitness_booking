package com.lhj.FitnessBooking.jwt;

import com.lhj.FitnessBooking.jwt.dto.Jwt;
import com.lhj.FitnessBooking.jwt.dto.RefreshTokenRequest;
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

    @PatchMapping("/api/refresh/token")
    public HttpEntity<Jwt> refreshTokens(@RequestBody RefreshTokenRequest refreshTokenRequest) {

        Jwt jwt = jwtService.refreshTokens(refreshTokenRequest.getRefreshToken());
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }
        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }
}
