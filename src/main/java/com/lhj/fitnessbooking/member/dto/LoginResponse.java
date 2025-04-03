package com.lhj.FitnessBooking.member.dto;

import com.lhj.FitnessBooking.domain.MemberGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private MemberGrade grade;
}
