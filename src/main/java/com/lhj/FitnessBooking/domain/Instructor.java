package com.lhj.FitnessBooking.domain;

import jakarta.persistence.*;

@Entity
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instructor_id")
    private Long id;

    private String name;

    private String imgUrl;
}
