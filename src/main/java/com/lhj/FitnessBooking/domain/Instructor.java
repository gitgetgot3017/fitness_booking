package com.lhj.FitnessBooking.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instructor_id")
    private Long id;

    private String name;

    private String imgUrl;

    public Instructor(String name) {
        this.name = name;
    }
}
