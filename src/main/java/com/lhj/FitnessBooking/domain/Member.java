package com.lhj.FitnessBooking.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String memberNum;

    private String phone;

    private boolean gender;

    private LocalDate regDate;

    public Member(String memberNum, String phone, boolean gender, LocalDate regDate) {
        this.memberNum = memberNum;
        this.phone = phone;
        this.gender = gender;
        this.regDate = regDate;
    }
}
