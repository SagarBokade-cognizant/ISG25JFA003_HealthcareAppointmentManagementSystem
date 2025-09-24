package com.cognizant.hams.service;

import com.cognizant.hams.dto.Request.MedicalRecordDTO;
import com.cognizant.hams.dto.Response.MedicalRecordResponseDTO;
import com.cognizant.hams.entity.Appointment;
import com.cognizant.hams.entity.Doctor;
import com.cognizant.hams.entity.MedicalRecord;
import com.cognizant.hams.entity.Patient;
import com.cognizant.hams.exception.APIException;
import com.cognizant.hams.repository.AppointmentRepository;
import com.cognizant.hams.repository.DoctorRepository;
import com.cognizant.hams.repository.MedicalRecordRepository;
import com.cognizant.hams.repository.PatientRepository;
import com.cognizant.hams.service.Impl.MedicalRecordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MedicalRecordServiceImpl medicalRecordService;

    private Patient patient;
    private Doctor doctor;
    private Appointment appointment;
    private MedicalRecordDTO medicalRecordDTO;
    private MedicalRecord medicalRecord;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setPatientId(1L);
        patient.setName("Test Patient");

        doctor = new Doctor();
        doctor.setDoctorId(101L);
        doctor.setDoctorName("Test Doctor");

        appointment = new Appointment();
        appointment.setAppointmentId(1001L);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        medicalRecordDTO = new MedicalRecordDTO(1001L, 1L, 101L, "Checkup", "Healthy", "No issues");

        medicalRecord = new MedicalRecord();
        medicalRecord.setRecordId(1L);
        medicalRecord.setPatient(patient);
        medicalRecord.setDoctor(doctor);
    }

    @Test
    void testCreateRecord_Success() {

        given(appointmentRepository.findById(1001L)).willReturn(Optional.of(appointment));
        given(patientRepository.findById(1L)).willReturn(Optional.of(patient));
        given(doctorRepository.findById(101L)).willReturn(Optional.of(doctor));
        given(medicalRecordRepository.save(any(MedicalRecord.class))).willReturn(medicalRecord);

        MedicalRecordResponseDTO result = medicalRecordService.createRecord(medicalRecordDTO);


        assertThat(result).isNotNull();
        assertThat(result.getPatientId()).isEqualTo(1L);
        assertThat(result.getDoctorName()).isEqualTo("Test Doctor");
        verify(medicalRecordRepository, times(1)).save(any(MedicalRecord.class));
    }

    @Test
    void testCreateRecord_ThrowsAPIException_WhenPatientMismatch() {

        Patient wrongPatient = new Patient();
        wrongPatient.setPatientId(2L);
        appointment.setPatient(wrongPatient);

        given(appointmentRepository.findById(1001L)).willReturn(Optional.of(appointment));
        given(patientRepository.findById(1L)).willReturn(Optional.of(patient));
        given(doctorRepository.findById(101L)).willReturn(Optional.of(doctor));


        assertThrows(APIException.class, () -> medicalRecordService.createRecord(medicalRecordDTO));
    }

    @Test
    void testGetRecordsForPatient() {

        given(patientRepository.existsById(1L)).willReturn(true);
        given(medicalRecordRepository.findByPatient_PatientIdOrderByCreatedAtDesc(1L))
                .willReturn(Collections.singletonList(medicalRecord));


        List<MedicalRecordResponseDTO> result = medicalRecordService.getRecordsForPatient(1L);


        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPatientName()).isEqualTo("Test Patient");
    }
}