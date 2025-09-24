package com.cognizant.hams.controller;

import com.cognizant.hams.dto.Request.AppointmentDTO;
import com.cognizant.hams.dto.Response.AppointmentResponseDTO;
import com.cognizant.hams.service.AppointmentService;
import com.cognizant.hams.service.DoctorService;
import com.cognizant.hams.service.NotificationService;
import com.cognizant.hams.service.PatientService; // Or a dedicated AppointmentService
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AppointmentController {

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final NotificationService notificationService;
    private final AppointmentService appointmentService;

    @PostMapping("/patients/{patientId}/appointments")
    public ResponseEntity<AppointmentResponseDTO> bookAppointment(
            @PathVariable("patientId") Long patientId,
            @Valid @RequestBody AppointmentDTO appointmentDTO) {
        AppointmentResponseDTO newAppointment = appointmentService.bookAppointment(patientId, appointmentDTO);
        return new ResponseEntity<>(newAppointment, HttpStatus.CREATED);
    }

    @GetMapping("/patients/{patientId}/status")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsForPatient(
            @PathVariable("patientId") Long patientId) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsForPatient(patientId);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Retrieves a single appointment by its unique ID.
     */
    @GetMapping("/appointments/{appointmentId}")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(
            @PathVariable("appointmentId") Long appointmentId) {
        AppointmentResponseDTO appointment = appointmentService.getAppointmentById(appointmentId);
        return ResponseEntity.ok(appointment);
    }

    /**
     * Updates an existing appointment's details.
     */
    @PutMapping("/appointments/{appointmentId}")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(
            @PathVariable("appointmentId") Long appointmentId,
            @Valid @RequestBody AppointmentDTO appointmentUpdateDTO) {
        AppointmentResponseDTO updatedAppointment = appointmentService.updateAppointment(appointmentId, appointmentUpdateDTO);
        return ResponseEntity.ok(updatedAppointment);
    }

    /**
     * Cancels an appointment.
     * PATCH is used as it's a partial update (changing the status).
     */
    @PatchMapping("/appointments/{appointmentId}/cancel")
    public ResponseEntity<AppointmentResponseDTO> cancelAppointment(
            @PathVariable("appointmentId") Long appointmentId) {
        AppointmentResponseDTO canceledAppointment = appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.ok(canceledAppointment);
    }

    @PostMapping("/doctors/{doctorId}/appointments/{appointmentId}/confirm")
    public ResponseEntity<AppointmentResponseDTO> confirmAppointment(@PathVariable Long doctorId, @PathVariable Long appointmentId){
        AppointmentResponseDTO responseDTO = appointmentService.confirmAppointment(doctorId, appointmentId);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/doctors/{doctorId}/appointments/{appointmentId}/reject")
    public ResponseEntity<AppointmentResponseDTO> rejectAppointment(@PathVariable Long doctorId,
                                                                    @PathVariable Long appointmentId,
                                                                    @RequestParam(value = "reason", required = false) String reason){
        AppointmentResponseDTO responseDTO = appointmentService.rejectAppointment(doctorId, appointmentId, reason);
        return ResponseEntity.ok(responseDTO);
    }

}