package com.lhj.fitnessbooking.domain.registration.controller;

import com.lhj.fitnessbooking.domain.registration.service.RegistrationService;
import com.lhj.fitnessbooking.domain.registration.dto.RegisterCourseDto;
import com.lhj.fitnessbooking.domain.registration.dto.RegisterInstructorDto;
import com.lhj.fitnessbooking.domain.member.domain.Member;
import com.lhj.fitnessbooking.domain.member.repository.MemberRepository;
import com.lhj.fitnessbooking.domain.member.exception.NotExistMemberException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/register")
public class RegistrationController {

    private final MemberRepository memberRepository;
    private final RegistrationService adminService;

    @PostMapping("/courses")
    public void registerCourses(HttpServletRequest request,  @RequestBody RegisterCourseDto registerCourseDto) {

        String memberNum = (String) request.getAttribute("memberNum");
        Member member = memberRepository.findByMemberNum(memberNum)
                .orElseThrow(() -> new NotExistMemberException("해당 멤버는 존재하지 않습니다."));

        adminService.registerCourses(member, registerCourseDto);
    }

    @PostMapping("/instructors")
    public void registerInstructor(@ModelAttribute RegisterInstructorDto registerInstructorDto) {

        adminService.registerInstructor(registerInstructorDto);
    }
}
