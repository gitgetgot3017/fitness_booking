package com.lhj.fitnessbooking.domain.course.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CourseMainHeader {

    private String memberName;
    private String memberNum;
    private String subscriptionName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int reservedCount;
    private int completedCount;
    private int availableCount;
}
