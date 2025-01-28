package com.lhj.FitnessBooking.course;

import com.lhj.FitnessBooking.domain.DayOfWeek;
import com.lhj.FitnessBooking.domain.History;
import com.lhj.FitnessBooking.domain.Member;
import com.lhj.FitnessBooking.dto.CourseInfo;
import com.lhj.FitnessBooking.dto.CourseInfoTmp;
import com.lhj.FitnessBooking.dto.CourseMainHeader;
import com.lhj.FitnessBooking.history.HistoryRepository;
import com.lhj.FitnessBooking.response.CourseMainResponse;
import com.lhj.FitnessBooking.subscription.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.lhj.FitnessBooking.domain.CourseStatus.ENROLLED;
import static com.lhj.FitnessBooking.domain.CourseStatus.RESERVED;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final SubscriptionRepository subscriptionRepository;
    private final HistoryRepository historyRepository;
    private final CourseRepository courseRepository;

    public CourseMainResponse showCourseMain(Member member, int year, int month, int week, DayOfWeek dayOfWeek) {

        CourseMainHeader courseMainHeader = subscriptionRepository.getSubscription(member, LocalDate.now());
        List<History> history = historyRepository.getHistory(member, year, month, ENROLLED, RESERVED);
        List<CourseInfoTmp> courses = courseRepository.getCourses(week, dayOfWeek, LocalTime.now());

        return changeIntoCourseMainResponse(courseMainHeader, history, courses);
    }

    private CourseMainResponse changeIntoCourseMainResponse(CourseMainHeader courseMainHeader, List<History> historyList, List<CourseInfoTmp> courses) {

        return new CourseMainResponse(
                courseMainHeader.getMemberName(),
                courseMainHeader.getMemberNum(),
                courseMainHeader.getSubscriptionName(),
                courseMainHeader.getStartDate(),
                courseMainHeader.getEndDate(),
                courseMainHeader.getCompletedCount(),
                courseMainHeader.getAvailableCount(),
                courseMainHeader.getReservedCount(),
                changeHistoryToDate(historyList),
                changeCourseInfoTmpToCourseInfo(courses));
    }

    private Set<Integer> changeHistoryToDate(List<History> historyList) {

        Set<Integer> dates = new HashSet<>();
        for (History history : historyList) {
            dates.add(history.getRegDateTime().getDayOfMonth());
        }
        return dates;
    }

    private List<CourseInfo> changeCourseInfoTmpToCourseInfo(List<CourseInfoTmp> courseInfoTmpList) {

        List<CourseInfo> courseInfoList = new ArrayList<>();
        for (CourseInfoTmp courseInfoTmp : courseInfoTmpList) {
            courseInfoList.add(new CourseInfo(
                    courseInfoTmp.getInstructorName(),
                    courseInfoTmp.getCourseName(),
                    courseInfoTmp.getCourseStartTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                    courseInfoTmp.getCourseStartTime().plusMinutes(50).format(DateTimeFormatter.ofPattern("HH:mm")),
                    courseInfoTmp.getAttendeeCount()
            ));
        }
        return courseInfoList;
    }
}
