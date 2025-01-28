package com.lhj.FitnessBooking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class CourseInfoTmp {

    private String instructorName;
    private String courseName;
    private LocalTime courseStartTime;
    private int attendeeCount;
}
