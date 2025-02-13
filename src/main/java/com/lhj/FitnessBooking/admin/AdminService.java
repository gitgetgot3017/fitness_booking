package com.lhj.FitnessBooking.admin;

import com.lhj.FitnessBooking.admin.dto.RegisterCourseDto;
import com.lhj.FitnessBooking.admin.dto.ScheduleRegistrationException;
import com.lhj.FitnessBooking.course.CourseRepository;
import com.lhj.FitnessBooking.domain.Course;
import com.lhj.FitnessBooking.domain.DayOfWeek;
import com.lhj.FitnessBooking.domain.Instructor;
import com.lhj.FitnessBooking.domain.Member;
import com.lhj.FitnessBooking.instructor.InstructorRepository;
import com.lhj.FitnessBooking.instructor.exception.NotExistInstructorException;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final Scheduler scheduler;

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
}
