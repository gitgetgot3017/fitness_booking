package com.lhj.FitnessBooking.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private int year;

    private int month;

    private int week;

    private LocalDateTime regDateTime;

    private CourseStatus status;
}
