package com.lhj.FitnessBooking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class CourseInfoTmp {

    private Long courseId;
    private String instructorName;
    private String instructorImgUrl;
    private String courseName;
    private LocalTime courseStartTime;
    private LocalTime courseEndTime;
    private int attendeeCount;
}
