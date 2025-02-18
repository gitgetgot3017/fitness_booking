package com.lhj.FitnessBooking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CourseInfo {

    private Long courseId;
    private String instructorName;
    private String instructorImgUrl;
    private String courseName;
    private String courseStartTime;
    private String courseEndTime;
    private int attendeeCount;
}
