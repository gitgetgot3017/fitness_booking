package com.lhj.fitnessbooking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    private LocalDate courseDate;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime reserveDateTime;

    public Reservation(LocalDate courseDate, Course course, Member member, LocalDateTime reserveDateTime) {
        this.courseDate = courseDate;
        this.course = course;
        this.member = member;
        this.reserveDateTime = reserveDateTime;
    }
}
