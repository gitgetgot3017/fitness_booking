package com.lhj.fitnessbooking.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhj.fitnessbooking.api.dto.*;
import com.lhj.fitnessbooking.api.exception.NotExistModelException;
import com.lhj.fitnessbooking.course.CourseRepository;
import com.lhj.fitnessbooking.dto.CourseInfoTmp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GeminiService {

    private final GeminiInterface geminiInterface;
    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper;

    @Value("${googleai.api.key}")
    private String API_KEY;

    public RecommendDto recommendCourse(MemberInputRequest request) {

        List<CourseInfoTmp> courses = courseRepository.getCourses(LocalDate.now(), LocalTime.now());

        MessageDto messageDto = getMessage(request, courses);
        if (!messageDto.isFlag()) { // Gemini를 사용하지 않는 경우
            return new RecommendDto(messageDto.getMessage(), null);
        }

        String modelName = extractModelName();
        if (modelName == null) {
            throw new NotExistModelException("적절한 Gemini 모델이 존재하지 않습니다.");
        }
        GeminiRequest geminiRequest = new GeminiRequest(messageDto.getMessage());
        GeminiResponse response = geminiInterface.getCompletion(modelName, geminiRequest);

        String geminiSaid = response.getCandidates()
                .stream()
                .findFirst()
                .flatMap(candidate -> candidate.getContent().getParts()
                        .stream()
                        .findFirst()
                        .map(GeminiResponse.TextPart::getText))
                .orElse(null);
        String courseName = extractCourseName(courses, geminiSaid);
        return new RecommendDto(geminiSaid, courseName);
    }

    private MessageDto getMessage(MemberInputRequest request, List<CourseInfoTmp> courses) {

        boolean flag = false;
        String message;
        if (courses.size() == 0) {
            message = "오늘의 수업이 존재하지 않습니다!"; // Gemini 이용 필요 x
        } else if (courses.size() == 1) {
            message = "오늘의 수업이 1개 남았습니다."; // Gemini 이용 필요 x
        } else {
            flag = true;
            message = String.format("오늘 컨디션은 %s, 목표는 %s일 때 적절한 요가를 추천해줘. 오늘 수업에는 %s가 있어. 추천 수업 1개와 그 이유를 간단하게 설명해줘.",
                    request.getCondition(), request.getGoal(), getCoursesName(courses));
        }
        return new MessageDto(flag, message);
    }

    private String getAvailableModels() {

        String targetUrl = "https://generativelanguage.googleapis.com/v1/models?key=" + API_KEY;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(targetUrl, String.class).getBody();
    }

    private String extractModelName() {

        String availableModels = getAvailableModels();

        try {
        JsonNode models = objectMapper.readTree(availableModels).get("models");

        if (models != null && models.isArray()) {
            for (JsonNode model : models) {
                JsonNode methods = model.get("supportedGenerationMethods");
                String description = model.has("description") ? model.get("description").asText().toLowerCase() : "";

                if (description.contains("deprecate")) {
                    continue;
                }

                if (methods != null && methods.isArray()) {
                    for (JsonNode method : methods) {
                        if (method.asText().equals("generateContent")) {
                            return model.get("name").asText().replace("models/", "");  // 🔥 사용 가능한 모델명 반환
                        }
                    }
                }
            }
        }
    } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null; // 적절한 모델이 존재하지 않는 경우
    }

    private String getCoursesName(List<CourseInfoTmp> courses) {

        StringBuilder sb = new StringBuilder();
        for (CourseInfoTmp course : courses) {
            sb.append(course.getCourseName() + " ");
        }
        return sb.toString();
    }

    private String extractCourseName(List<CourseInfoTmp> courses, String geminiSaid) {

        if (geminiSaid == null) { // Gemini의 응답이 없는 경우
            return null;
        }

        String courseName = null;
        for (CourseInfoTmp course : courses) {
            if (geminiSaid.contains(course.getCourseName())) {
                courseName = course.getCourseName();
                break;
            }
        }
        return courseName;
    }
}
