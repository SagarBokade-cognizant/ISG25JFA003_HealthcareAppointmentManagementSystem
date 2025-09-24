package com.cognizant.hams.service;

import com.cognizant.hams.dto.Request.PatientDTO;
import com.cognizant.hams.dto.Response.DoctorResponseDTO;
import com.cognizant.hams.dto.Response.PatientResponseDTO;
import com.cognizant.hams.entity.Patient;
import com.cognizant.hams.exception.APIException;
import com.cognizant.hams.exception.ResourceNotFoundException;
import com.cognizant.hams.repository.PatientRepository;
import com.cognizant.hams.service.DoctorService;
import com.cognizant.hams.service.Impl.PatientServiceImpl;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @InjectMocks
    private PatientServiceImpl patientService;

    @Mock
    private PatientRepository patientRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private DoctorService doctorService;

    private Patient patient;
    private PatientDTO patientDTO;
    private PatientResponseDTO patientResponseDTO;

    @BeforeEach
    void setUp() {
        // Initialize common objects for tests
        patientDTO = new PatientDTO("John Doe", null, "Male", "1234567890", "john.doe@example.com", "123 Main St", "O+");
        patient = new Patient(1L, null, "John Doe", null, "Male", "1234567890", "john.doe@example.com", "123 Main St", "O+");
        patientResponseDTO = new PatientResponseDTO(1L, "John Doe", "john.doe@example.com", "1234567890", "123 Main St", "Male", null, "O+");
    }

    @Test
    void testCreatePatient_Success() {

        when(patientRepository.existsByEmailAndContactNumber(anyString(), anyString())).thenReturn(false);
        when(modelMapper.map(patientDTO, Patient.class)).thenReturn(patient);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        when(modelMapper.map(patient, PatientResponseDTO.class)).thenReturn(patientResponseDTO);

        PatientResponseDTO result = patientService.createPatient(patientDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("John Doe");
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void testCreatePatient_ThrowsAPIException() {

        when(patientRepository.existsByEmailAndContactNumber(anyString(), anyString())).thenReturn(true);
        when(modelMapper.map(patientDTO, Patient.class)).thenReturn(patient);

        assertThrows(APIException.class, () -> patientService.createPatient(patientDTO));
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void testGetPatientById_ValidId() {

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(modelMapper.map(patient, PatientResponseDTO.class)).thenReturn(patientResponseDTO);

        PatientResponseDTO result = patientService.getPatientById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getPatientId()).isEqualTo(1L);
    }

    @Test
    void testGetPatientById_InvalidId() {

        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> patientService.getPatientById(99L));
    }

    @Test
    void testUpdatePatient() {

        PatientDTO updateDetails = new PatientDTO();
        updateDetails.setName("Johnathan Doe");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        when(modelMapper.map(patient, PatientResponseDTO.class)).thenReturn(patientResponseDTO);

        patientService.updatePatient(1L, updateDetails);


        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void testDeletePatient() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        doNothing().when(patientRepository).delete(patient);

        patientService.deletePatient(1L);

        verify(patientRepository, times(1)).delete(patient);
    }

    @Test
    void testGetAllDoctors() {
        List<DoctorResponseDTO> doctors = Collections.singletonList(new DoctorResponseDTO());
        when(doctorService.getAllDoctor()).thenReturn(doctors);

        List<DoctorResponseDTO> result = patientService.getAllDoctors();

        assertThat(result).hasSize(1);
        verify(doctorService, times(1)).getAllDoctor();
    }
}