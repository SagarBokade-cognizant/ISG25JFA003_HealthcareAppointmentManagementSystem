package com.cognizant.hams.controller;

import com.cognizant.hams.dto.NotificationDTO;
import com.cognizant.hams.dto.NotificationResponseDTO;
import com.cognizant.hams.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponseDTO> createNotification(@Valid @RequestBody NotificationDTO notificationDTO){
        NotificationResponseDTO notificationResponseDTO = notificationService.createNotification(notificationDTO);
        return new ResponseEntity<>(notificationResponseDTO,HttpStatus.CREATED);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<NotificationResponseDTO>> getPatient(@PathVariable("patientId") Long patientId){
        List<NotificationResponseDTO> notificationResponseDTOS = notificationService.getPatientNotifications(patientId);
        return new ResponseEntity<>(notificationResponseDTOS, HttpStatus.OK);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<NotificationResponseDTO>> getDoctor(@PathVariable("doctorId") Long doctorId){
        List<NotificationResponseDTO> notificationResponseDTOS = notificationService.getDoctorNotifications(doctorId);
        return new ResponseEntity<>(notificationResponseDTOS, HttpStatus.OK);
    }

    @PutMapping("/{id}/read")
    public void markRead(@PathVariable Long id){
        notificationService.markAsRead(id);
    }
}
