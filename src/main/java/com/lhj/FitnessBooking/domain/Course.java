package com.lhj.FitnessBooking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Entity
@NoArgsConstructor
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

    public Course(Instructor instructor, String name, DayOfWeek dayOfWeek, LocalTime startTime) {
        this.instructor = instructor;
        this.name = name;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
    }
}
