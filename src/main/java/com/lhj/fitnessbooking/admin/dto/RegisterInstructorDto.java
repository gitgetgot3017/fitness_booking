package com.lhj.fitnessbooking.admin.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class RegisterInstructorDto {

    private String instructorName;
    private MultipartFile image;
}
