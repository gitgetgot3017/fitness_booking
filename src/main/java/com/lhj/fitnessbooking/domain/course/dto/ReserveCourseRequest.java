package com.lhj.fitnessbooking.domain.course.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReserveCourseRequest {

    private LocalDate date;
    private Long courseId;
}
