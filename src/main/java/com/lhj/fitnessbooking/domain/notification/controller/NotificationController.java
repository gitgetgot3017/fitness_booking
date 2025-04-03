package com.lhj.fitnessbooking.domain.notification.controller;

import com.lhj.fitnessbooking.domain.notification.repository.NotificationRepository;
import com.lhj.fitnessbooking.domain.notification.service.NotificationService;
import com.lhj.fitnessbooking.domain.notification.domain.Notification;
import com.lhj.fitnessbooking.global.auth.exception.NotExistAccessTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;

    @GetMapping(produces = "text/event-stream")
    public HttpEntity<SseEmitter> connectSse(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "accessToken", required = false) String accessToken, @RequestHeader(value = "Last-Event-ID", defaultValue = "0") long lastEventId) {

        // /api/notifications (SSE 연결 요청)에 대한 accessToken 처리
        if (request.getRequestURI().equals("/api/notifications") && accessToken == null) {
            throw new NotExistAccessTokenException("로그인이 필요합니다.");
        }

        response.setContentType("text/event-stream");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000"); // CORS 문제를 해결하기 위함

        SseEmitter emitter = notificationService.connectSse(lastEventId);
        return new ResponseEntity<>(emitter, HttpStatus.OK);
    }

    @GetMapping("/history")
    public HttpEntity<List<Notification>> getNotifications() {

        List<Notification> notifications = notificationRepository.getWeeklyNotifications(LocalDateTime.now().minusDays(7));
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }
}
