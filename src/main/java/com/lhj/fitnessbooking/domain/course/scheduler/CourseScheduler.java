package com.lhj.fitnessbooking.domain.course.scheduler;

import com.lhj.fitnessbooking.domain.course.domain.Course;
import com.lhj.fitnessbooking.domain.course.repository.CourseRepository;
import com.lhj.fitnessbooking.domain.coursehistory.domain.CourseHistory;
import com.lhj.fitnessbooking.domain.coursehistory.repository.CourseHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static com.lhj.fitnessbooking.domain.course.domain.DayOfWeek.*;

@Component
@RequiredArgsConstructor
public class CourseScheduler {

    private final CourseRepository courseRepository;
    private final CourseHistoryRepository courseHistoryRepository;

    // 매일 오전 8시에 동작하는 스케줄러
    @Scheduled(cron = "0 0 8 * * *")
    public void openCourses() {

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        DayOfWeek tomorrowDayOfWeek = tomorrow.getDayOfWeek();

        List<Course> courses = courseRepository.findByDayOfWeek(changeToMyDayOfWeek(tomorrowDayOfWeek));
        for (Course course : courses) {
            courseHistoryRepository.save(new CourseHistory(course, tomorrow, 0));
        }
    }

    private com.lhj.fitnessbooking.domain.course.domain.DayOfWeek changeToMyDayOfWeek(DayOfWeek dayOfWeek) {

        switch (dayOfWeek) {
            case MONDAY: return MON;
            case TUESDAY: return TUES;
            case WEDNESDAY: return WED;
            case THURSDAY: return THUR;
            case FRIDAY: return FRI;
            case SATURDAY: return SAT;
            default: throw new IllegalArgumentException("일요일은 지원하지 않습니다.");
        }
    }
}
