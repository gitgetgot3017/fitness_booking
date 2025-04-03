package com.lhj.FitnessBooking.member;

import com.lhj.FitnessBooking.member.dto.LoginResponse;
import com.lhj.FitnessBooking.member.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public HttpEntity<LoginResponse> login(@RequestBody LoginRequest joinRequest) {

        LoginResponse loginResponse = memberService.login(joinRequest);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
}
