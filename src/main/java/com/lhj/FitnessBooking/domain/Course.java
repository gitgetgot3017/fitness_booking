package com.lhj.FitnessBooking.domain;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    private String name;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private LocalTime startTime;
}
