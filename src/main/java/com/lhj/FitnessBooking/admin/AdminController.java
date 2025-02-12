package com.lhj.FitnessBooking.admin;

import com.lhj.FitnessBooking.admin.dto.RegisterCourseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/register")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/courses")
    public void registerCourses(@RequestBody RegisterCourseDto registerCourseDto) {

        adminService.registerCourses(registerCourseDto);
    }
}
