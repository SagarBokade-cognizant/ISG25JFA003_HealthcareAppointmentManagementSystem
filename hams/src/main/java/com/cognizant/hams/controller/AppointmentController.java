package com.cognizant.hams.controller;

import com.cognizant.hams.dto.request.AppointmentDTO;
import com.cognizant.hams.dto.response.AppointmentResponseDTO;
import com.cognizant.hams.service.AppointmentService;
import com.cognizant.hams.service.DoctorService;
import com.cognizant.hams.service.NotificationService;
import com.cognizant.hams.service.PatientService; // Or a dedicated AppointmentService
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final PatientService patientService;

    @PostMapping("/patients/me/appointments")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<AppointmentResponseDTO> bookAppointment(
            @Valid @RequestBody AppointmentDTO appointmentDTO) {
        AppointmentResponseDTO newAppointment = appointmentService.bookAppointment(appointmentDTO);
        return new ResponseEntity<>(newAppointment, HttpStatus.CREATED);
    }

    @GetMapping("/patients/me/status")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsForPatient() {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsForPatient();
        return ResponseEntity.ok(appointments);
    }

//    @GetMapping("/doctors/me/appointments")
//    @PreAuthorize("hasRole('DOCTOR')")
//    public ResponseEntity<List<AppointmentResponseDTO>> getDoctorAppointments() {
//        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsForDoctor();
//        return new ResponseEntity<>(appointments, HttpStatus.OK);
//    }

    @GetMapping("/appointments/{appointmentId}")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(
            @PathVariable("appointmentId") Long appointmentId) {
        AppointmentResponseDTO appointment = appointmentService.getAppointmentById(appointmentId);
        return ResponseEntity.ok(appointment);
    }

    @PatchMapping("patients/me/appointments/{appointmentId}")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(
            @PathVariable("appointmentId") Long appointmentId,
            @Valid @RequestBody AppointmentDTO appointmentUpdateDTO) {
        AppointmentResponseDTO updatedAppointment = appointmentService.updateAppointment(appointmentId, appointmentUpdateDTO);
        return ResponseEntity.ok(updatedAppointment);
    }

    @PatchMapping("/appointments/{appointmentId}/cancel")
    public ResponseEntity<AppointmentResponseDTO> cancelAppointment(
            @PathVariable("appointmentId") Long appointmentId) {
        AppointmentResponseDTO canceledAppointment = appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.ok(canceledAppointment);
    }

    @PostMapping("/doctors/me/appointments/{appointmentId}/confirm")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<AppointmentResponseDTO> confirmAppointment(@PathVariable Long appointmentId){
        AppointmentResponseDTO responseDTO = appointmentService.confirmAppointment(appointmentId);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/doctors/me/appointments/{appointmentId}/reject")
    public ResponseEntity<AppointmentResponseDTO> rejectAppointment(@PathVariable Long appointmentId,
                                                                    @RequestParam(value = "reason", required = false) String reason){
        AppointmentResponseDTO responseDTO = appointmentService.rejectAppointment(appointmentId, reason);
        return ResponseEntity.ok(responseDTO);
    }


    @GetMapping("/appointments/today-count")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<Long> getTodayAppointmentCount() {
        long count = appointmentService.getTodayAppointmentCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/dashboard/patient-count")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<Long> getTotalPatientCount() {
        long count = patientService.getTotalPatientCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/doctors/dashboard/pending-reviews/count")
    @PreAuthorize("hasRole('DOCTOR')") // Restrict to logged-in Doctor
    public ResponseEntity<Long> getPendingReviewsCountForDoctor() {
        // Get the current doctor's username from the security context
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        long count = appointmentService.getPendingReviewsCountForDoctor(currentUsername);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/doctors/appointments")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsForDoctor() {
        // 🔑 Assumes the service will use SecurityContextHolder to get the doctor's ID
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsForDoctor();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/doctors/appointments/today")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<AppointmentResponseDTO>> getTodayAppointmentsForDoctor() {
        List<AppointmentResponseDTO> appointments = appointmentService.getTodayAppointmentsForDoctor();
        return ResponseEntity.ok(appointments);
    }
}