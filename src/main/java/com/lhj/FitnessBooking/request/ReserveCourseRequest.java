package com.lhj.FitnessBooking.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReserveCourseRequest {

    private LocalDate date;
    private Long courseId;
}
