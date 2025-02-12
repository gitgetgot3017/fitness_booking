package com.lhj.FitnessBooking.admin.dto;

import com.lhj.FitnessBooking.domain.DayOfWeek;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
public class RegisterCourseDto {

    private String courseName;
    private String instructorName;
    private List<DayOfWeek> dayOfWeeks;
    private LocalTime startTime;
    private LocalTime endTime;
}
