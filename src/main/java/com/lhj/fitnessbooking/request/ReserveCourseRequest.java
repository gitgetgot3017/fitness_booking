package com.lhj.fitnessbooking.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReserveCourseRequest {

    private LocalDate date;
    private Long courseId;
}
