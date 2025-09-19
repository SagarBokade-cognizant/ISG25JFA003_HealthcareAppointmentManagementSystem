package com.cognizant.hams.service;

import com.cognizant.hams.dto.Response.NotificationResponseDTO;
import com.cognizant.hams.entity.Appointment;

import java.util.List;

public interface NotificationService {
    void notifyDoctorOnAppointmentRequest(Appointment appointment);
    void notifyPatientOnAppointmentDecision(Appointment appointment, boolean confirmed, String reason);
    List<NotificationResponseDTO> getNotificationForDoctor(Long doctorId);
    List<NotificationResponseDTO> getNotificationForPatient(Long patientId);
    void markAsRead(Long notificationId);
}
