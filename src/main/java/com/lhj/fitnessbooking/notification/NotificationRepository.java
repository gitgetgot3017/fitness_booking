package com.lhj.fitnessbooking.notification;

import com.lhj.fitnessbooking.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select n " +
            "from Notification n " +
            "where :startDateTime <= n.notificationDateTime " +
            "order by n.notificationDateTime desc")
    List<Notification> getWeeklyNotifications(@Param("startDateTime") LocalDateTime startDateTime);
}
