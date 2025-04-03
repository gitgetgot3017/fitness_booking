package com.lhj.fitnessbooking.domain.member.dto;

import lombok.Getter;

@Getter
public class LoginRequest {

    private String memberNum;

    private String password;
}
