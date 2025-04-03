package com.lhj.fitnessbooking.domain.course.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class CourseDetailResponse {

    private boolean ifSuccess;
    private CourseDetailInfo courseDetailInfo;
    private Map<String, String> errorResponse;
}
