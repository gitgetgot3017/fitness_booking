package com.lhj.FitnessBooking.notification;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SseEmitterRepository {

    private Map<String, SseEmitter> emitterStore = new ConcurrentHashMap<>();

    public void saveEmitter(String id, SseEmitter sseEmitter) {
        emitterStore.put(id, sseEmitter);
    }

    public void deleteEmitter(String id) {
        emitterStore.remove(id);
    }

    public Set<Map.Entry<String, SseEmitter>> findAllEmitters() {
        return emitterStore.entrySet();
    }
}
