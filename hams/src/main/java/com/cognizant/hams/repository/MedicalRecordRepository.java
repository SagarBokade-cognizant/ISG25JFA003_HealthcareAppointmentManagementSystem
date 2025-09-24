package com.cognizant.hams.repository;

import com.cognizant.hams.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByPatient_PatientIdOrderByCreatedAtDesc(Long patientId);
    List<MedicalRecord> findByDoctor_DoctorIdOrderByCreatedAtDesc(Long doctorId);
}
