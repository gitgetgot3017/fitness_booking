package com.lhj.fitnessbooking.domain.course.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class CourseHistoryTmp {

    private LocalDate courseDate;
    private String instructorName;
    private String courseName;
    private LocalTime startTime;
    private int attendeeCount;
}
