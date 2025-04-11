package com.lhj.fitnessbooking.domain.course.scheduler;

import com.lhj.fitnessbooking.domain.course.domain.Course;
import com.lhj.fitnessbooking.domain.course.dto.CourseMainHeader;
import com.lhj.fitnessbooking.domain.course.exception.NotExistCourseException;
import com.lhj.fitnessbooking.domain.course.repository.CourseRepository;
import com.lhj.fitnessbooking.domain.history.domain.History;
import com.lhj.fitnessbooking.domain.history.repository.HistoryRepository;
import com.lhj.fitnessbooking.domain.member.domain.Member;
import com.lhj.fitnessbooking.domain.member.exception.NotExistMemberException;
import com.lhj.fitnessbooking.domain.member.repository.MemberRepository;
import com.lhj.fitnessbooking.domain.notification.service.NotificationService;
import com.lhj.fitnessbooking.domain.subscription.repository.SubscriptionRepository;

import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.lhj.fitnessbooking.domain.history.domain.CourseStatus.ENROLLED;

@Component
@RequiredArgsConstructor
@Transactional
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

        CourseMainHeader subscription = subscriptionRepository.getSubscription(member, LocalDate.now(), (Pageable) PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElse(null); // TODO: subscriptinoRepository의 subscription 가져오는 두 메서드 수정해야 함
        if (subscription.getCompletedCount() + 4 >= subscription.getAvailableCount()) {
            notificationService.sendData(subscription.getMemberName() + "(" + subscription.getMemberName() + ") 님의 이용권이" + (subscription.getAvailableCount() - subscription.getCompletedCount() - 1) + "회 남았습니다.");
        }

        subscriptionRepository.changeCount(member);
    }
}
