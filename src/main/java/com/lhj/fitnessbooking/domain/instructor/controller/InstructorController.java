package com.lhj.fitnessbooking.domain.instructor.controller;

import com.lhj.fitnessbooking.domain.instructor.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    @GetMapping
    public HttpEntity<List<String>> getInstructorNames() {

        List<String> instructorNames = instructorService.getInstructorNames();
        return new ResponseEntity<>(instructorNames, HttpStatus.OK);
    }
}
