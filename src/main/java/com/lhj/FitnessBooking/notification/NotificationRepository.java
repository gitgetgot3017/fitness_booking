package com.lhj.FitnessBooking.notification;

import com.lhj.FitnessBooking.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
