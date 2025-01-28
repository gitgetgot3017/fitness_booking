package com.lhj.FitnessBooking.course;

import com.lhj.FitnessBooking.domain.DayOfWeek;
import com.lhj.FitnessBooking.domain.Member;
import com.lhj.FitnessBooking.member.MemberRepository;
import com.lhj.FitnessBooking.response.CourseMainResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {

    private final MemberRepository memberRepository;
    private final CourseService courseService;

    @GetMapping
    public CourseMainResponse showCourseMain(int year, int month, int week, DayOfWeek dayOfWeek) {

        Member member = memberRepository.findById(1L).get(); // TODO: member를 어떻게 구할 것인가
        return courseService.showCourseMain(member, year, month, week, dayOfWeek);
    }
}
