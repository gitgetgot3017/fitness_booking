package com.lhj.fitnessbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CourseHistoryDto {

    private LocalDate courseDate;
    private String instructorName;
    private String courseName;
    private String startTime;
    private String endTime;
    private int attendeeCount;
}
