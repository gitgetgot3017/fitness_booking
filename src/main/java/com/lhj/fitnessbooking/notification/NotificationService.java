package com.lhj.fitnessbooking.notification;

import com.lhj.fitnessbooking.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private static final long DEFAULT_TIMEOUT = 1000 * 60 * 60L; // 1시간

    private final SseEmitterRepository sseEmitterRepository;
    private final NotificationRepository notificationRepository;

    /**
     * 1. SSE 객체 생성 후 저장 -> 반환
     * 2. 더미 데이터 전송 (503을 막기 위함)
     * 3. 미전송 데이터 전송
     */
    public SseEmitter connectSse(long lastEventId) {

        String id = String.valueOf(System.currentTimeMillis());

        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        sseEmitterRepository.saveEmitter(id, emitter);
        emitter.onCompletion(() -> sseEmitterRepository.deleteEmitter(id));
        emitter.onTimeout(() -> sseEmitterRepository.deleteEmitter(id));

        sendToClient(emitter, -1, "{\"data\": \"dummy data\"}", id);

        if(lastEventId > 0) {
            notificationRepository.findAll().stream()
                    .filter(notification -> lastEventId < notification.getId())
                    .forEach(notification -> sendToClient(emitter, notification.getId(), notification, id));
        }

        return emitter;
    }

    public void sendData(String content) {

        Notification notification = new Notification(content, LocalDateTime.now());
        notificationRepository.save(notification);

        Set<Map.Entry<String, SseEmitter>> entrySet = sseEmitterRepository.findAllEmitters();
        for (Map.Entry<String, SseEmitter> entry : entrySet) {
            sendToClient(entry.getValue(), notification.getId(), notification, entry.getKey());
        }
    }

    private void sendToClient(SseEmitter sseEmitter, long notificationId, Object notification, String id) {

        try {
            sseEmitter.send(SseEmitter.event()
                    .id(String.valueOf(notificationId))
                    .name("message")
                    .data(notification));
        } catch (IOException e) {
            sseEmitterRepository.deleteEmitter(id);
        }
    }
}
