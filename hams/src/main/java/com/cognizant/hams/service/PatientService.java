package com.cognizant.hams.service;

import com.cognizant.hams.dto.*;

import java.util.List;

public interface PatientService {
//    --- Patient Profile ---
    PatientResponseDTO createPatient(PatientCreatedDTO patientCreatedDTO);
    PatientResponseDTO getPatientById(Long patientId);
    PatientResponseDTO updatePatient(Long patientId, PatientUpdateDTO patientUpdateDTO);
    PatientResponseDTO deletePatient(Long patientId);

//    --- Doctor Search (from patient perspective) ---
    List<DoctorResponseDTO> findAllDoctorsByPatient();
    List<DoctorResponseDTO> searchDoctorByName(String name);
    List<DoctorResponseDTO> searchDoctorBySpecialization(String specialization);
    List<DoctorResponseDTO> searchDoctors(String name, String specialization);

//  --- Appointments ---
//    AppointmentResponseDTO getAppointmentById(Long appointmentId);
//    AppointmentResponseDTO bookAppointment(Long patientId, AppointmentCreateDTO appointmentCreateDTO);
//    AppointmentResponseDTO updateAppointment(Long appointmentId, AppointmentUpdateDTO appointmentUpdateDTO);
//    AppointmentResponseDTO cancelAppointment(Long appointmentId);

//    --- Doctor Availability (helper) ---
//    List<AvailabilityDTO> getDoctorAvailability(Long doctorId);

}
