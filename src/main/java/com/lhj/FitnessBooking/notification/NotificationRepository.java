package com.lhj.FitnessBooking.notification;

import com.lhj.FitnessBooking.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select n " +
            "from Notification n " +
            "where :startDateTime <= n.notificationDateTime ")
    List<Notification> getWeeklyNotifications(@Param("startDateTime") LocalDateTime startDateTime);
}
