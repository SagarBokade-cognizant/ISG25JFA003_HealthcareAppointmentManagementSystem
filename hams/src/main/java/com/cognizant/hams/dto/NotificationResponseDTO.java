package com.cognizant.hams.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponseDTO {
    private Long id;
    private String message;
    private String type;
    private boolean readStatus;
    private LocalDateTime createdAt;
    private Long doctorId;
    private Long patientId;
}
