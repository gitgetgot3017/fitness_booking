package com.lhj.fitnessbooking.domain.registration.dto;

import com.lhj.fitnessbooking.domain.course.domain.DayOfWeek;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.List;

@Getter
public class RegisterCourseDto {

    private String courseName;

    private String instructorName;

    private List<DayOfWeek> dayOfWeeks;

    @DateTimeFormat(pattern = "HH:mm")
    private List<LocalTime> startTime;

    @DateTimeFormat(pattern = "HH:mm")
    private List<LocalTime> endTime;
}
