package com.cognizant.hams.service;

import com.cognizant.hams.dto.NotificationDTO;
import com.cognizant.hams.dto.NotificationResponseDTO;
import com.cognizant.hams.entity.Notification;

import java.util.List;

public interface NotificationService {
    NotificationResponseDTO createNotification(NotificationDTO notificationDTO);
    List<NotificationResponseDTO> getPatientNotifications(Long patientId);
    List<NotificationResponseDTO> getDoctorNotifications(Long doctorId);
    void markAsRead(Long notification);
}
