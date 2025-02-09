package com.lhj.FitnessBooking.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Jwt {

    private String accessToken;
    private String refreshToken;
}
