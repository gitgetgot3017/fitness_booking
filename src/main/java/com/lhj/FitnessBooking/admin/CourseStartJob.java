package com.lhj.FitnessBooking.admin;

import com.lhj.FitnessBooking.course.CourseRepository;
import com.lhj.FitnessBooking.course.exception.NotExistCourseException;
import com.lhj.FitnessBooking.domain.Course;
import com.lhj.FitnessBooking.domain.History;
import com.lhj.FitnessBooking.domain.Member;
import com.lhj.FitnessBooking.dto.CourseMainHeader;
import com.lhj.FitnessBooking.history.HistoryRepository;
import com.lhj.FitnessBooking.member.MemberRepository;
import com.lhj.FitnessBooking.member.exception.NotExistMemberException;
import com.lhj.FitnessBooking.notification.NotificationService;
import com.lhj.FitnessBooking.subscription.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.lhj.FitnessBooking.domain.CourseStatus.ENROLLED;

@Component
@RequiredArgsConstructor
public class CourseStartJob implements Job {

    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;
    private final HistoryRepository historyRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final NotificationService notificationService;

    /**
     * 수업이 시작하면
     * 1. history에 ENROLLED를 남긴다.
     * 2. subscription의 completed_count 1 증가시키고, reserved_count 1 감소시킨다.
     * 3. subscription의 (available_count - completed_count)가 3회 이하로 남은 경우, 관리자에게 알림을 보낸다.
     */
    @Override
    public void execute(JobExecutionContext context) {

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        long memberId = dataMap.getLong("memberId");
        long courseId = dataMap.getLong("courseId");
        String courseDateString = dataMap.getString("courseDate");
        LocalDate courseDate = LocalDate.parse(courseDateString);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotExistMemberException("존재하지 않는 회원입니다."));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotExistCourseException("해당 수업은 존재하지 않습니다"));

        History history = new History(member, courseDate, course, courseDate.getYear(), courseDate.getMonthValue(), LocalDateTime.now(), ENROLLED);
        historyRepository.save(history);

        subscriptionRepository.changeCount(member);

        CourseMainHeader subscription = subscriptionRepository.getSubscription(member, LocalDate.now()); // TODO: subscriptinoRepository의 subscription 가져오는 두 메서드 수정해야 함
        if (subscription.getCompletedCount() + 3 >= subscription.getAvailableCount()) {
            notificationService.sendData(subscription.getMemberName() + "(" + subscription.getMemberName() + ") 님의 이용권이" + (subscription.getAvailableCount() - subscription.getCompletedCount()) + "회 남았습니다.");
        }
    }
}
