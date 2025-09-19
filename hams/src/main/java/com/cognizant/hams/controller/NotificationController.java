package com.cognizant.hams.controller;

import com.cognizant.hams.dto.Response.NotificationResponseDTO;
import com.cognizant.hams.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/patients/{patientId}/notification")
    public ResponseEntity<List<NotificationResponseDTO>> getPatientNotifications(@PathVariable Long patientId){
        return ResponseEntity.ok(notificationService.getNotificationForPatient(patientId));
    }

    @GetMapping("/doctors/{doctorId}/notification")
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationsForDoctor(@PathVariable("doctorId") Long doctorId) {
        List<NotificationResponseDTO> notifications = notificationService.getNotificationForDoctor(doctorId);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable("notificationId") Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }
}
