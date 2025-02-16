package com.lhj.FitnessBooking.notification;

import com.lhj.FitnessBooking.domain.Member;
import com.lhj.FitnessBooking.member.MemberRepository;
import com.lhj.FitnessBooking.member.exception.NotExistMemberException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    @GetMapping(produces = "text/event-stream")
    public HttpEntity<SseEmitter> connectSse(HttpServletRequest request, @RequestHeader(value = "Last-Event-ID", defaultValue = "0") long lastEventId) {

        Member member = getMember(request);

        SseEmitter emitter = notificationService.connectSse(member.getId(), lastEventId);
        return new ResponseEntity<>(emitter, HttpStatus.OK);
    }

    private Member getMember(HttpServletRequest request) {

        String memberNum = (String) request.getAttribute("memberNum");
        return memberRepository.findByMemberNum(memberNum)
                .orElseThrow(() -> new NotExistMemberException("해당 멤버는 존재하지 않습니다."));
    }
}
