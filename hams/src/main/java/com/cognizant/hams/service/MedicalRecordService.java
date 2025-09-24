package com.cognizant.hams.service;

import com.cognizant.hams.dto.Request.MedicalRecordDTO;
import com.cognizant.hams.dto.Response.MedicalRecordResponseDTO;

import java.util.List;
public interface MedicalRecordService {
    MedicalRecordResponseDTO createRecord(MedicalRecordDTO dto);
    List<MedicalRecordResponseDTO> getRecordsForPatient(Long patientId);
    List<MedicalRecordResponseDTO> getRecordsForDoctor(Long doctorId);
}