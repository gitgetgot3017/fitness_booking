package com.lhj.FitnessBooking.dto;

import lombok.AllArgsConstructor;

import java.time.LocalTime;

@AllArgsConstructor
public class CourseInfo {

    private String instructorName;
    private String courseName;
    private LocalTime courseStartTime;
    private int attendeeCount;
}
