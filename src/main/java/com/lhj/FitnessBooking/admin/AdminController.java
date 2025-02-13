package com.lhj.FitnessBooking.admin;

import com.lhj.FitnessBooking.admin.dto.RegisterCourseDto;
import com.lhj.FitnessBooking.domain.Member;
import com.lhj.FitnessBooking.member.MemberRepository;
import com.lhj.FitnessBooking.member.exception.NotExistMemberException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/register")
public class AdminController {

    private final MemberRepository memberRepository;
    private final AdminService adminService;

    @PostMapping("/courses")
    public void registerCourses(HttpServletRequest request,  @RequestBody RegisterCourseDto registerCourseDto) {

        String memberNum = (String) request.getAttribute("memberNum");
        Member member = memberRepository.findByMemberNum(memberNum)
                .orElseThrow(() -> new NotExistMemberException("해당 멤버는 존재하지 않습니다."));

        adminService.registerCourses(member, registerCourseDto);
    }
}
