package com.cognizant.hams.repository;

import com.cognizant.hams.entity.Notification;
import com.cognizant.hams.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByPatient_PatientIdOrderByCreatedAtDesc(Long patientId);
    List<Notification> findByDoctor_DoctorIdOrderByCreatedAtDesc(Long doctorId);
}