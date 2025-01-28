package com.lhj.FitnessBooking.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class CourseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_history_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private LocalDateTime dateTime;

    private int count; // 수강한 인원

    public CourseHistory(Course course, LocalDateTime dateTime, int count) {
        this.course = course;
        this.dateTime = dateTime;
        this.count = count;
    }
}
