package com.lhj.fitnessbooking.instructor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InstructorRepositoryTest {

    @Autowired InstructorRepository instructorRepository;

    @DisplayName("강사 이름으로 강사 찾기")
    @Test
    void findByName() {

        // given
        String instructorName = "지수";

        // when, then
        instructorRepository.findByName(instructorName)
                .isPresent();
    }
}