package com.lhj.fitnessbooking.domain.course.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class Top3CourseRepository {

    private Map<Long, AtomicInteger> courseScores = new ConcurrentHashMap<>();

    public void increaseScore(Long courseId) {
        courseScores.computeIfAbsent(courseId, k -> new AtomicInteger(0))
                .incrementAndGet();
    }

    public void decreaseScore(Long courseId) {
        courseScores.computeIfAbsent(courseId, k -> new AtomicInteger(0))
                .decrementAndGet();
    }

    public List<Long> getPopularTop3CourseIds() {
        return courseScores.entrySet().stream()
                .sorted((a, b) -> b.getValue().get() - a.getValue().get())
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();
    }
}
