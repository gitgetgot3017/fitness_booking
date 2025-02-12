package com.lhj.FitnessBooking.admin;

import com.lhj.FitnessBooking.admin.dto.RegisterCourseDto;
import com.lhj.FitnessBooking.course.CourseRepository;
import com.lhj.FitnessBooking.domain.Course;
import com.lhj.FitnessBooking.domain.DayOfWeek;
import com.lhj.FitnessBooking.domain.Instructor;
import com.lhj.FitnessBooking.instructor.InstructorRepository;
import com.lhj.FitnessBooking.instructor.exception.NotExistInstructorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;

    public void registerCourses(RegisterCourseDto registerCourseDto) {

        String instructorName = registerCourseDto.getInstructorName();
        Instructor instructor = instructorRepository.findByName(instructorName)
                .orElseThrow(() -> new NotExistInstructorException("존재하지 않는 강사입니다."));

        for (DayOfWeek dayOfWeek : registerCourseDto.getDayOfWeeks()) {
            Course course = new Course(instructor, registerCourseDto.getCourseName(), dayOfWeek, registerCourseDto.getStartTime());
            courseRepository.save(course);
        }
    }
}
