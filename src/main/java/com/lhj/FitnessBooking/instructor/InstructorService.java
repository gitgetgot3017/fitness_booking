package com.lhj.FitnessBooking.instructor;

import com.lhj.FitnessBooking.domain.Instructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InstructorService {

    private final InstructorRepository instructorRepository;

    public List<String> getInstructorNames() {

        return instructorRepository.findAll().stream()
                .map(Instructor::getName)
                .collect(Collectors.toList());
    }
}
