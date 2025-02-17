package com.lhj.FitnessBooking.admin;

import com.lhj.FitnessBooking.admin.dto.RegisterCourseDto;
import com.lhj.FitnessBooking.admin.dto.RegisterInstructorDto;
import com.lhj.FitnessBooking.admin.exception.ImageSaveFailureException;
import com.lhj.FitnessBooking.admin.exception.ScheduleRegistrationException;
import com.lhj.FitnessBooking.course.CourseRepository;
import com.lhj.FitnessBooking.domain.Course;
import com.lhj.FitnessBooking.domain.DayOfWeek;
import com.lhj.FitnessBooking.domain.Instructor;
import com.lhj.FitnessBooking.domain.Member;
import com.lhj.FitnessBooking.instructor.InstructorRepository;
import com.lhj.FitnessBooking.instructor.exception.NotExistInstructorException;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final Scheduler scheduler;

    @Value("${file.dir}")
    private String fileDir;

    public void registerCourses(Member member, RegisterCourseDto registerCourseDto) {

        String instructorName = registerCourseDto.getInstructorName();
        Instructor instructor = instructorRepository.findByName(instructorName)
                .orElseThrow(() -> new NotExistInstructorException("존재하지 않는 강사입니다."));

        for (DayOfWeek dayOfWeek : registerCourseDto.getDayOfWeeks()) {
            for (LocalTime startTime: registerCourseDto.getStartTime()) {

                Course course = new Course(instructor, registerCourseDto.getCourseName(), dayOfWeek, startTime);
                courseRepository.save(course);

                scheduleJob(member, dayOfWeek, startTime, course);
            }
        }
    }

    private void scheduleJob(Member member, DayOfWeek dayOfWeek, LocalTime startTime, Course course) {

        JobDetail jobDetail = JobBuilder.newJob(CourseStartJob.class)
                .usingJobData("memberId", member.getId())
                .usingJobData("courseId", course.getId())
                .usingJobData("courseDate", LocalDate.now().toString())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(generateCronExpression(startTime) + " ? * " + getQuartzDayOfWeek(dayOfWeek)))
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new ScheduleRegistrationException("quartz 스케줄러 등록에 실패하였습니다.");
        }
    }

    private String generateCronExpression(LocalTime startTime) {

        return String.format("%d %d %d", startTime.getSecond(), startTime.getMinute(), startTime.getHour());
    }

    private String getQuartzDayOfWeek(DayOfWeek dayOfWeek) {

        return switch (dayOfWeek) {
            case MON -> "2";
            case TUES -> "3";
            case WED -> "4";
            case THUR -> "5";
            case FRI -> "6";
            case SAT -> "7";
        };
    }

    public void registerInstructor(RegisterInstructorDto registerInstructorDto) {

        String imageUrl = storeImage(registerInstructorDto.getImage());

        Instructor instructor = new Instructor(registerInstructorDto.getInstructorName(), imageUrl);
        instructorRepository.save(instructor);
    }

    private String storeImage(MultipartFile file) {

        String imageUrl = createStoreFileName(file.getOriginalFilename());
        try {
            file.transferTo(new File(getFullPath(imageUrl)));
            return imageUrl;
        } catch (IOException e) {
            throw new ImageSaveFailureException("이미지 저장에 실패하였습니다.");
        }
    }

    private String createStoreFileName(String originalFilename) {

        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }
    private String extractExt(String originalFilename) {

        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    private String getFullPath(String filename) {

        return fileDir + filename;
    }
}
