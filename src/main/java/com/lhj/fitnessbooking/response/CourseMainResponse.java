package com.lhj.FitnessBooking.response;

import com.lhj.FitnessBooking.dto.CourseInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
public class CourseMainResponse {

    private String memberName;
    private String memberNum;
    private String subscriptionName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int completedCount; // 수강한 횟수
    private int availableCount; // 수강 가능한 횟수
    private int reservedCount; // 예약한 횟수

    private Set<Integer> courseMainHistoryList;

    private List<CourseInfo> courses;
}
