package com.lhj.FitnessBooking.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
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

    @Enumerated(EnumType.STRING)
    private CourseStatus status;

    public History(Member member, Course course, int year, int month, int week, LocalDateTime regDateTime, CourseStatus status) {
        this.member = member;
        this.course = course;
        this.year = year;
        this.month = month;
        this.week = week;
        this.regDateTime = regDateTime;
        this.status = status;
    }
}
