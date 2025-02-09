package com.lhj.FitnessBooking.course;

import com.lhj.FitnessBooking.domain.Member;
import com.lhj.FitnessBooking.dto.CourseDetailResponse;
import com.lhj.FitnessBooking.dto.CourseHistoryDto;
import com.lhj.FitnessBooking.member.MemberRepository;
import com.lhj.FitnessBooking.member.exception.NotExistMemberException;
import com.lhj.FitnessBooking.request.ReserveCourseRequest;
import com.lhj.FitnessBooking.response.CourseMainResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {

    private final MemberRepository memberRepository;
    private final CourseService courseService;

    @GetMapping
    public CourseMainResponse showCourseMain(HttpServletRequest request, @RequestParam("date") LocalDate date) {

        Member member = getMember(request, memberRepository);
        return courseService.showCourseMain(member, date);
    }

    @GetMapping("/detail")
    public CourseDetailResponse showCourseDetail(HttpServletRequest request, @RequestParam LocalDate date, @RequestParam Long courseId) {

        Member member = getMember(request, memberRepository);
        return courseService.showCourseDetail(member, date, courseId);
    }

    @PostMapping("/reservations")
    public void reserveCourse(HttpServletRequest request, @RequestBody ReserveCourseRequest courseRequest) {

        Member member = getMember(request, memberRepository);
        courseService.reserveCourse(member, courseRequest.getDate(), courseRequest.getCourseId());
    }

    @PostMapping("/notifications")
    public void waitCourse(HttpServletRequest request, @RequestBody ReserveCourseRequest courseRequest) {

        Member member = getMember(request, memberRepository);
        courseService.waitCourse(member, courseRequest.getDate(), courseRequest.getCourseId());
    }

    @PostMapping("/cancellation")
    public void cancelCourse(HttpServletRequest request, @RequestBody ReserveCourseRequest courseRequest) {

        Member member = getMember(request, memberRepository);
        courseService.cancelCourse(member, courseRequest.getDate(), courseRequest.getCourseId());
    }

    @DeleteMapping("/notifications/cancellation")
    public void cancelWaiting(HttpServletRequest request, @RequestBody ReserveCourseRequest courseRequest) {

        Member member = getMember(request, memberRepository);
        courseService.cancelWaiting(courseRequest.getDate(), courseRequest.getCourseId(), member);
    }

    @GetMapping("/history")
    public List<CourseHistoryDto> showCourseHistory(HttpServletRequest request, @RequestParam LocalDate date) {

        Member member = getMember(request, memberRepository);
        return courseService.showCourseHistory(member, date);
    }

    private Member getMember(HttpServletRequest request, MemberRepository memberRepository) {

        String memberNum = (String) request.getAttribute("memberNum");
        return memberRepository.findByMemberNum(memberNum)
                .orElseThrow(() -> new NotExistMemberException("해당 멤버는 존재하지 않습니다."));
    }
}
