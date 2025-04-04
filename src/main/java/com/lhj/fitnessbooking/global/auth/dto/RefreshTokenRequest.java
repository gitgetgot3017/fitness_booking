package com.lhj.fitnessbooking.global.auth.dto;

import lombok.Getter;

@Getter
public class RefreshTokenRequest {

    private String accessToken;
    private String refreshToken;
}
