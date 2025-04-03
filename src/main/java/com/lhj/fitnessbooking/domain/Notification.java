package com.lhj.fitnessbooking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private String content;

    private LocalDateTime notificationDateTime;

    public Notification(String content, LocalDateTime notificationDateTime) {
        this.content = content;
        this.notificationDateTime = notificationDateTime;
    }
}
