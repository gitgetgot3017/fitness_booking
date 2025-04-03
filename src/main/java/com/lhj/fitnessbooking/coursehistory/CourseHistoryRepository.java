package com.lhj.fitnessbooking.coursehistory;

import com.lhj.fitnessbooking.domain.CourseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseHistoryRepository extends JpaRepository<CourseHistory, Long> {
}
