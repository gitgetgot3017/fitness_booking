package com.lhj.fitnessbooking.domain.course.service;

import com.lhj.fitnessbooking.domain.course.exception.SendSmsFailException;
import com.lhj.fitnessbooking.domain.course.dto.CourseInfo;
import com.lhj.fitnessbooking.dto.CourseInfoTmp;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class SmsService {

    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @Value("${coolsms.api.number}")
    private String fromPhoneNumber;

    private DefaultMessageService defaultMessageService;

    private final CourseRepository courseRepository;

    @PostConstruct
    public void init() {
         defaultMessageService = NurigoApp.INSTANCE.initialize(
            apiKey,
            apiSecret,
            "https://api.coolsms.co.kr"
        );
    }

    public void sendSms(String toPhoneNumber, LocalDate date, Long courseId) {

        CourseInfoTmp courseInfoTmp = courseRepository.getCourseDetailCourseInfo(date, courseId)
                .orElseThrow();
        CourseInfo courseInfo = changeCourseInfoTmpToCourseInfo(courseInfoTmp);

        Message message = new Message();
        message.setFrom(fromPhoneNumber);
        message.setTo(toPhoneNumber);
        message.setText("이현지필라테스 그룹 6:1 [" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "/" + courseInfo.getCourseStartTime() + "-" + courseInfo.getCourseEndTime() + "/" + courseInfo.getInstructorName() + "(" + courseInfo.getCourseName() + ")] 수업 예약이 가능합니다.");

        try {
            defaultMessageService.send(message);
        } catch (NurigoMessageNotReceivedException | NurigoEmptyResponseException | NurigoUnknownException e) {
            throw new SendSmsFailException("메시지 전송 실패");
        }
    }

    private CourseInfo changeCourseInfoTmpToCourseInfo(CourseInfoTmp courseInfoTmp) {
        return new CourseInfo(
                courseInfoTmp.getCourseId(),
                courseInfoTmp.getInstructorName(),
                courseInfoTmp.getInstructorImgUrl(),
                courseInfoTmp.getCourseName(),
                courseInfoTmp.getCourseStartTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                courseInfoTmp.getCourseStartTime().plusMinutes(50).format(DateTimeFormatter.ofPattern("HH:mm")),
                courseInfoTmp.getAttendeeCount()
        );
    }
}
