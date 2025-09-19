package com.cognizant.hams.service;

import com.cognizant.hams.dto.AppointmentDTO;
import com.cognizant.hams.dto.AppointmentResponseDTO;

import java.util.List;

public interface AppointmentService {
    AppointmentResponseDTO confirmAppointment(Long doctorId, Long appointmentId);
    AppointmentResponseDTO rejectAppointment(Long doctorId, Long appointmentId, String reason);
    AppointmentResponseDTO bookAppointment(Long patientId, AppointmentDTO appointmentDTO);
    AppointmentResponseDTO updateAppointment(Long appointmentId, AppointmentDTO appointmentUpdateDTO);
    AppointmentResponseDTO cancelAppointment(Long appointmentId);
    AppointmentResponseDTO getAppointmentById(Long appointmentId);
    List<AppointmentResponseDTO> getAppointmentsForPatient(Long patientId);

}
