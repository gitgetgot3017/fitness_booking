package com.lhj.fitnessbooking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
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

    private LocalDate courseDate;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private int year;

    private int month;

    private LocalDateTime regDateTime;

    @Enumerated(EnumType.STRING)
    private CourseStatus status;

    public History(Member member, LocalDate courseDate, Course course, int year, int month, LocalDateTime regDateTime, CourseStatus status) {
        this.member = member;
        this.courseDate = courseDate;
        this.course = course;
        this.year = year;
        this.month = month;
        this.regDateTime = regDateTime;
        this.status = status;
    }
}
