package com.lhj.FitnessBooking.api;

import com.lhj.FitnessBooking.api.dto.GeminiRequest;
import com.lhj.FitnessBooking.api.dto.GeminiResponse;
import com.lhj.FitnessBooking.api.dto.MemberInputRequest;
import com.lhj.FitnessBooking.api.dto.MessageDto;
import com.lhj.FitnessBooking.course.CourseRepository;
import com.lhj.FitnessBooking.dto.CourseInfoTmp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GeminiService {

    private final GeminiInterface geminiInterface;
    private final CourseRepository courseRepository;

    public String recommendCourse(MemberInputRequest request) {

        MessageDto messageDto = getMessage(request);
        if (!messageDto.isFlag()) { // Gemini를 사용하지 않는 경우
            return messageDto.getMessage();
        }

        GeminiRequest geminiRequest = new GeminiRequest(messageDto.getMessage());
        GeminiResponse response = geminiInterface.getCompletion("gemini-pro", geminiRequest);

        return response.getCandidates()
                .stream()
                .findFirst()
                .flatMap(candidate -> candidate.getContent().getParts()
                        .stream()
                        .findFirst()
                        .map(GeminiResponse.TextPart::getText))
                .orElse(null);
    }

    private MessageDto getMessage(MemberInputRequest request) {

        List<CourseInfoTmp> courses = courseRepository.getTodayCourses(LocalDate.now(), LocalTime.now());

        boolean flag = false;
        String message;
        if (courses.size() == 0) {
            message = "오늘의 수업이 존재하지 않습니다!"; // Gemini 이용 필요 x
        } else if (courses.size() == 1) {
            message = "오늘의 수업이 1개 남았습니다. 해당 수업을 예약하시겠습니까?"; // Gemini 이용 필요 x
        } else {
            flag = true;
            message = String.format("오늘 컨디션은 %s, 목표는 %s일 때 적절한 요가를 추천해줘. 오늘 수업에는 %s가 있어. 추천 수업 1개와 그 이유를 간단하게 설명해줘.",
                    request.getCondition(), request.getGoal(), getCoursesName(courses));
        }
        return new MessageDto(flag, message);
    }

    private String getCoursesName(List<CourseInfoTmp> courses) {

        StringBuilder sb = new StringBuilder();
        for (CourseInfoTmp course : courses) {
            sb.append(course.getCourseName() + " ");
        }
        return sb.toString();
    }
}
