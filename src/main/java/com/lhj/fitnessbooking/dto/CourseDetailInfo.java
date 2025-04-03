package com.lhj.fitnessbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CourseDetailInfo {

    private String memberName;
    private String memberNum;
    private String subscriptionName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int completedCount; // 수강한 횟수
    private int availableCount; // 수강 가능한 횟수
    private int reservedCount; // 예약한 횟수

    private String courseDate;
    private CourseInfo course;

    private int cancelableCount;
}
