package com.lhj.fitnessbooking.domain.coursehistory.repository;

import com.lhj.fitnessbooking.domain.coursehistory.domain.CourseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseHistoryRepository extends JpaRepository<CourseHistory, Long> {
}
