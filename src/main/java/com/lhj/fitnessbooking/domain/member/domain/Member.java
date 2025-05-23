package com.lhj.fitnessbooking.domain.member.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String memberNum;

    private String password;

    private String name;

    private String phone;

    private boolean gender;

    @Enumerated(EnumType.STRING)
    private MemberGrade grade;

    private LocalDate regDate;

    public Member(String memberNum, String password, String name, String phone, boolean gender, MemberGrade grade, LocalDate regDate) {
        this.memberNum = memberNum;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.grade = grade;
        this.regDate = regDate;
    }
}
