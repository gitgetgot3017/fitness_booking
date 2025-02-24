package com.lhj.FitnessBooking.courseHistory;

import com.lhj.FitnessBooking.domain.CourseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseHistoryRepository extends JpaRepository<CourseHistory, Long> {
}
