package com.lhj.FitnessBooking.course;

import com.lhj.FitnessBooking.domain.Member;
import com.lhj.FitnessBooking.dto.CourseDetailResponse;
import com.lhj.FitnessBooking.member.MemberRepository;
import com.lhj.FitnessBooking.request.ReserveCourseRequest;
import com.lhj.FitnessBooking.response.CourseMainResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/reservations")
    public void reserveCourse(@RequestBody ReserveCourseRequest request) {

        Member member = memberRepository.findById(1L).get(); // TODO: member를 어떻게 구할 것인가
        courseService.reserveCourse(member, request.getDate(), request.getCourseId());
    }

    @PostMapping("/notifications")
    public void waitCourse(@RequestBody ReserveCourseRequest request) {

        Member member = memberRepository.findById(1L).get(); // TODO: member를 어떻게 구할 것인가
        courseService.waitCourse(member, request.getDate(), request.getCourseId());
    }

    @PostMapping("/cancellation")
    public void cancelCourse(@RequestBody ReserveCourseRequest request) {

        Member member = memberRepository.findById(1L).get(); // TODO: member를 어떻게 구할 것인가
        courseService.cancelCourse(member, request.getDate(), request.getCourseId());
    }

    @DeleteMapping("/notifications/cancellation")
    public void cancelWaiting(@RequestBody ReserveCourseRequest request) {

        Member member = memberRepository.findById(1L).get(); // TODO: member를 어떻게 구할 것인가
        courseService.cancelWaiting(request.getDate(), request.getCourseId(), member);
    }

    @GetMapping("/history")
    public void showCourseHistory(@RequestParam LocalDate date) {

        Member member = memberRepository.findById(1L).get(); // TODO: member를 어떻게 구할 것인가
        courseService.showCourseHistory(member, date);
    }
}
