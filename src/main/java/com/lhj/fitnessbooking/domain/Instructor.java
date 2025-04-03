package com.lhj.fitnessbooking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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

    public Instructor(String name, String imgUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
    }
}
