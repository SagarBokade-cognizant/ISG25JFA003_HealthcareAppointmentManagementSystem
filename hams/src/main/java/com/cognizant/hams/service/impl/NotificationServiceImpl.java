package com.cognizant.hams.service.impl;

import com.cognizant.hams.dto.NotificationDTO;
import com.cognizant.hams.dto.NotificationResponseDTO;
import com.cognizant.hams.entity.Doctor;
import com.cognizant.hams.entity.Notification;
import com.cognizant.hams.entity.Patient;
import com.cognizant.hams.exception.APIException;
import com.cognizant.hams.exception.ResourceNotFoundException;
import com.cognizant.hams.repository.DoctorRepository;
import com.cognizant.hams.repository.NotificationRepository;
import com.cognizant.hams.repository.PatientRepository;
import com.cognizant.hams.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Override
    public NotificationResponseDTO createNotification(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        notification.setMessage(notificationDTO.getMessage());
        notification.setType(notificationDTO.getType());

        if (notificationDTO.getDoctorId() != null) {
            Doctor doctor = doctorRepository.findById(notificationDTO.getDoctorId())
                    .orElseThrow(() -> new APIException("Doctor not found"));
            notification.setDoctor(doctor);
        }
        if (notificationDTO.getPatientId() != null) {
            Patient patient = patientRepository.findById(notificationDTO.getPatientId())
                    .orElseThrow(() -> new APIException("Patient not found"));
            notification.setPatient(patient);
        }
        notification.setCreatedAt(LocalDateTime.now());
        Notification saved = notificationRepository.save(notification);
        return entityToResponse(saved);
    }

    @Override
    public List<NotificationResponseDTO> getPatientNotifications(Long patientId) {
        return notificationRepository.findByPatient_PatientIdOrderByCreatedAtDesc(patientId)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponseDTO> getDoctorNotifications(Long doctorId) {
        return notificationRepository.findByDoctor_DoctorIdOrderByCreatedAtDesc(doctorId)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "notificationId", notificationId));
        notification.setReadStatus(true);
        notificationRepository.save(notification);
    }

    // The id field in NotificationResponseDTO is not correct. Changed from id to notificationId.
    public NotificationResponseDTO entityToResponse(Notification notification){
        NotificationResponseDTO responseDTO = new NotificationResponseDTO();
        responseDTO.setId(notification.getNotificationId());
        responseDTO.setMessage(notification.getMessage());
        responseDTO.setType(notification.getType());
        responseDTO.setReadStatus(notification.isReadStatus());
        responseDTO.setCreatedAt(notification.getCreatedAt());
        responseDTO.setPatientId(notification.getPatient() != null ? notification.getPatient().getPatientId() : null);
        responseDTO.setDoctorId(notification.getDoctor() != null ? notification.getDoctor().getDoctorId() : null);
        return responseDTO;
    }
}