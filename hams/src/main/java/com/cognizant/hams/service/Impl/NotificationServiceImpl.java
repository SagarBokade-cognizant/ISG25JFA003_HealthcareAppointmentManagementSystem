package com.cognizant.hams.service.Impl;

import com.cognizant.hams.dto.Response.NotificationResponseDTO;
import com.cognizant.hams.entity.Appointment;
import com.cognizant.hams.entity.Notification;
import com.cognizant.hams.repository.NotificationRepository;
import com.cognizant.hams.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final ModelMapper modelMapper;

    @Override
    public void notifyDoctorOnAppointmentRequest(Appointment appointment) {
        Notification notification = new Notification();
        notification.setAppointment(appointment);
        notification.setRecipientType(Notification.RecipientType.DOCTOR);
        notification.setRecipientId(appointment.getDoctor().getDoctorId());
        notification.setTitle("New appointment request");
        notification.setMessage("Patient " + appointment.getPatient().getName() +
                " request an appointment on " + appointment.getAppointmentDate() +
                " from " + appointment.getStartTime() + "to " + appointment.getEndTime());
        notificationRepository.save(notification);
    }

    @Override
    public void notifyPatientOnAppointmentDecision(Appointment appointment,
                                                   boolean confirmed, String reason) {
        Notification notification = new Notification();
        notification.setAppointment(appointment);
        notification.setRecipientType(Notification.RecipientType.PATIENT);
        notification.setRecipientId(appointment.getPatient().getPatientId());
        notification.setTitle(confirmed ? "Appointment confirmed" : "Appointment rejected");
        String message = confirmed ? "Your appointment with Dr. " + appointment.getDoctor()
                .getDoctorName() + " on " + appointment.getAppointmentDate() + " is confirmed." :
                "Your appointment with " + appointment.getDoctor().getDoctorName() + " on " +
                        appointment.getAppointmentDate() + " was rejected. Reason: " +
                        (reason == null ? "N/A" : reason);
        notification.setMessage(message);
        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationResponseDTO> getNotificationForDoctor(Long doctorId) {
        List<Notification> notifications = notificationRepository.findByRecipientTypeAndRecipientIdOrderByCreatedAtDesc(Notification.RecipientType.DOCTOR, doctorId);
        return notifications.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponseDTO> getNotificationForPatient(Long patientId) {
        // Find notifications for the patient, ordered by creation date
        List<Notification> notifications = notificationRepository.findByRecipientTypeAndRecipientIdOrderByCreatedAtDesc(Notification.RecipientType.PATIENT, patientId);

        // Map the list of entities to a list of DTOs to avoid serialization errors
        return notifications.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }

    private NotificationResponseDTO mapToDTO(Notification notification) {
        NotificationResponseDTO dto = modelMapper.map(notification, NotificationResponseDTO.class);
        // Manually set appointmentId to avoid lazy-loading proxy error
        if (notification.getAppointment() != null) {
            dto.setAppointmentId(notification.getAppointment().getAppointmentId());
        }
        return dto;
    }
}
