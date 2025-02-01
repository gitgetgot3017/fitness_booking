package com.lhj.FitnessBooking.course;

import com.lhj.FitnessBooking.domain.Member;
import com.lhj.FitnessBooking.dto.CourseDetailResponse;
import com.lhj.FitnessBooking.member.MemberRepository;
import com.lhj.FitnessBooking.response.CourseMainResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {

    private final MemberRepository memberRepository;
    private final CourseService courseService;

    @GetMapping
    public CourseMainResponse showCourseMain(@RequestParam LocalDate date) {

        Member member = memberRepository.findById(1L).get(); // TODO: member를 어떻게 구할 것인가
        return courseService.showCourseMain(member, date);
    }

    @GetMapping("/detail")
    public CourseDetailResponse shorCourseDetail(@RequestParam LocalDate date, @RequestParam Long courseId) {

        Member member = memberRepository.findById(1L).get(); // TODO: member를 어떻게 구할 것인가
        return courseService.showCourseDetail(member, date, courseId);
    }
}
