package com.lhj.FitnessBooking.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String memberNum;

    private String phone;

    private boolean gender;

    private LocalDate regDate;
}
