package com.cognizant.hams.service;

import com.cognizant.hams.dto.Request.PatientDTO;
import com.cognizant.hams.dto.Response.DoctorResponseDTO;
import com.cognizant.hams.dto.Response.PatientResponseDTO;

import java.util.List;

public interface PatientService {
    PatientResponseDTO createPatient(PatientDTO patientCreateDTO);

    PatientResponseDTO getPatientById(Long patientId);

    PatientResponseDTO updatePatient(Long patientId, PatientDTO patientUpdateDTO);

    PatientResponseDTO deletePatient(Long patientId);

    //    --- Doctor Search (from patient perspective) ---
    List<DoctorResponseDTO> getAllDoctors();

//    List<DoctorResponseDTO> searchDoctorsByNameAndSpecialization(String name, String specialization);

    List<DoctorResponseDTO> searchDoctorByName(String name);

    List<DoctorResponseDTO> searchDoctorBySpecialization(String specialization);

}
//    AppointmentResponseDTO bookAppointment(Long patientId, AppointmentDTO appointmentDTO);
//    AppointmentResponseDTO cancelAppointment(Long appointmentId);

//  --- Appointments ---
//    AppointmentResponseDTO getAppointmentById(Long appointmentId);
//    AppointmentResponseDTO bookAppointment(Long patientId, AppointmentCreateDTO appointmentCreateDTO);
//    AppointmentResponseDTO updateAppointment(Long appointmentId, AppointmentUpdateDTO appointmentUpdateDTO);
//    AppointmentResponseDTO cancelAppointment(Long appointmentId);

//    --- Doctor Availability (helper) ---
//    List<AvailabilityDTO> getDoctorAvailability(Long doctorId);
