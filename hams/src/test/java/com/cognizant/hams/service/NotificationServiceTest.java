package com.cognizant.hams.service;

import com.cognizant.hams.dto.Response.NotificationResponseDTO;
import com.cognizant.hams.entity.Appointment;
import com.cognizant.hams.entity.Doctor;
import com.cognizant.hams.entity.Notification;
import com.cognizant.hams.entity.Patient;
import com.cognizant.hams.repository.NotificationRepository;
import com.cognizant.hams.service.Impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Appointment mockAppointment;
    private Notification mockNotification;

    @BeforeEach
    void setUp() {
        Patient patient = new Patient();
        patient.setPatientId(1L);
        patient.setName("John Doe");

        Doctor doctor = new Doctor();
        doctor.setDoctorId(101L);
        doctor.setDoctorName("Dr. Smith");

        mockAppointment = new Appointment();
        mockAppointment.setPatient(patient);
        mockAppointment.setDoctor(doctor);

        mockNotification = new Notification();
        mockNotification.setId(1L);
        mockNotification.setRead(false);
    }

    @Test
    void testNotifyDoctorOnAppointmentRequest() {

        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);

        notificationService.notifyDoctorOnAppointmentRequest(mockAppointment);

        verify(notificationRepository, times(1)).save(notificationCaptor.capture());
        Notification capturedNotification = notificationCaptor.getValue();
        assertThat(capturedNotification.getRecipientType()).isEqualTo(Notification.RecipientType.DOCTOR);
        assertThat(capturedNotification.getRecipientId()).isEqualTo(101L);
        assertThat(capturedNotification.getTitle()).isEqualTo("New appointment request");
    }

    @Test
    void testNotifyPatientOnAppointmentDecision_Confirmed() {

        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);

        notificationService.notifyPatientOnAppointmentDecision(mockAppointment, true, null);

        verify(notificationRepository, times(1)).save(notificationCaptor.capture());
        Notification captured = notificationCaptor.getValue();
        assertThat(captured.getRecipientType()).isEqualTo(Notification.RecipientType.PATIENT);
        assertThat(captured.getRecipientId()).isEqualTo(1L);
        assertThat(captured.getTitle()).isEqualTo("Appointment confirmed");
    }

    @Test
    void testGetNotificationForPatient() {

        given(notificationRepository.findByRecipientTypeAndRecipientIdOrderByCreatedAtDesc(Notification.RecipientType.PATIENT, 1L))
                .willReturn(Collections.singletonList(mockNotification));
        given(modelMapper.map(mockNotification, NotificationResponseDTO.class)).willReturn(new NotificationResponseDTO());

        List<NotificationResponseDTO> result = notificationService.getNotificationForPatient(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void testMarkAsRead() {

        given(notificationRepository.findById(1L)).willReturn(Optional.of(mockNotification));


        notificationService.markAsRead(1L);

        verify(notificationRepository, times(1)).save(mockNotification);
        assertThat(mockNotification.isRead()).isTrue();
    }
}