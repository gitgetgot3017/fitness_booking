package com.lhj.FitnessBooking.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long id;

    @ManyToOne
    @Column(name = "member_id")
    private Member member;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private int count;
}
