package com.lhj.FitnessBooking.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDate startDate;

    private LocalDate endDate;

    private int reservedCount; // 예약한 횟수

    private int completedCount; // 수강한 횟수

    private int availableCount; // 수강 가능한 횟수

    public Subscription(Member member, LocalDate startDate, LocalDate endDate, int reservedCount, int completedCount, int availableCount) {
        this.member = member;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reservedCount = reservedCount;
        this.completedCount = completedCount;
        this.availableCount = availableCount;
    }
}
