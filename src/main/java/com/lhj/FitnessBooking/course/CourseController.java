package com.lhj.FitnessBooking.course;

import com.lhj.FitnessBooking.course.exception.NotExistCourseException;
import com.lhj.FitnessBooking.domain.Course;
import com.lhj.FitnessBooking.domain.DayOfWeek;
import com.lhj.FitnessBooking.domain.Member;
import com.lhj.FitnessBooking.dto.CourseDetailResponse;
import com.lhj.FitnessBooking.dto.CourseHistoryDto;
import com.lhj.FitnessBooking.member.MemberRepository;
import com.lhj.FitnessBooking.member.exception.NotExistMemberException;
import com.lhj.FitnessBooking.request.ReserveCourseRequest;
import com.lhj.FitnessBooking.response.CourseMainResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.lhj.FitnessBooking.domain.DayOfWeek.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    private final MemberRepository memberRepository;
    private final CourseService courseService;
    private final CourseRepository courseRepository;

    @GetMapping
    public CourseMainResponse showCourseMain(HttpServletRequest request, @RequestParam("date") LocalDate date) {

        Member member = getMember(request, memberRepository);
        return courseService.showCourseMain(member, date);
    }

    @GetMapping("/detail")
    public CourseDetailResponse showCourseDetail(HttpServletRequest request, @RequestParam("date") LocalDate date, @RequestParam("courseId") Long courseId) {

        Member member = getMember(request, memberRepository);
        CourseDetailResponse tmp = courseService.showCourseDetail(member, date, courseId);
        return tmp;
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

    @GetMapping("/id")
    public HttpEntity<Long> getCoursesId(@RequestParam("name") String courseName) {

        DayOfWeek dayOfWeek = changeToMyDateOfWeek(LocalDate.now().getDayOfWeek());
        Optional<Course> courseExist = courseRepository.findByDayOfWeekAndName(dayOfWeek, courseName);
        if (courseExist.isEmpty()) {
            throw new NotExistCourseException("오늘 해당 시각에는 수업이 존재하지 않습니다.");
        }

        return new ResponseEntity<>(courseExist.get().getId(), HttpStatus.OK);
    }

    private DayOfWeek changeToMyDateOfWeek(java.time.DayOfWeek dayOfWeek) {

        return switch (dayOfWeek) {
            case MONDAY -> MON;
            case TUESDAY -> TUES;
            case WEDNESDAY -> WED;
            case THURSDAY -> THUR;
            case FRIDAY -> FRI;
            case SATURDAY -> SAT;
            default -> throw new IllegalArgumentException("일요일에는 수업이 존재하지 않습니다.");
        };
    }
}
