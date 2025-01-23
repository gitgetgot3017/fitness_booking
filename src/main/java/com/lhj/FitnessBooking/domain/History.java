package com.lhj.FitnessBooking.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;

    @ManyToOne
    @Column(name = "member_id")
    private Member member;

    private LocalDateTime regDateTime;
}
