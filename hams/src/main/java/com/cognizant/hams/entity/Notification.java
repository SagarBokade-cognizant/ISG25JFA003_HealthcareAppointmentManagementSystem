package com.cognizant.hams.entity;

import jakarta.persistence.*;
import lombok.Data;

import javax.print.Doc;
import java.time.LocalDateTime;

@Entity(name = "notification")
@Table(name = "notifications")
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private  String message;
    private String type;
    private boolean readStatus = false;
    private LocalDateTime createdAt = LocalDateTime.now();
}
