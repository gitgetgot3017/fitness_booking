package com.lhj.FitnessBooking.domain;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Long id;

    @ManyToOne
    @Column(name = "instructor_id")
    private Instructor instructor;

    @ManyToOne
    @Column(name = "member_id")
    private Member member;

    private String name;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private LocalTime startTime;
}
