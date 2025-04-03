package com.lhj.fitnessbooking.member.dto;

import com.lhj.fitnessbooking.domain.MemberGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private MemberGrade grade;
}
