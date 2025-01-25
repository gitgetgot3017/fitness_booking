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
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private int reservedCount; // 예약한 횟수

    private int completedCount; // 수강한 횟수

    private int availableCount; // 수강 가능한 횟수
}
