package com.cognizant.hams.serviceImpl;

import com.cognizant.hams.dto.AppointmentResponseDTO;
import com.cognizant.hams.dto.Request.DoctorAvailabilityDTO;
import com.cognizant.hams.dto.Request.DoctorDTO;
import com.cognizant.hams.dto.Response.DoctorAndAvailabilityResponseDTO;
import com.cognizant.hams.dto.Response.DoctorAvailabilityResponseDTO;
import com.cognizant.hams.dto.Response.DoctorResponseDTO;
import com.cognizant.hams.entity.Doctor;
import com.cognizant.hams.entity.DoctorAvailability;
import com.cognizant.hams.exception.ResourceNotFoundException;
import com.cognizant.hams.repository.DoctorAvailabilityRepository;
import com.cognizant.hams.repository.DoctorRepository;
import com.cognizant.hams.service.Impl.DoctorServiceImpl;
import com.cognizant.hams.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorAvailabilityRepository availabilityRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private Doctor doctor;
    private DoctorDTO doctorDTO;
    private DoctorResponseDTO doctorResponseDTO;
    private DoctorAvailabilityDTO availabilityDTO;
    private DoctorAvailability availability;
    private DoctorAvailabilityResponseDTO availabilityResponseDTO;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        doctor.setDoctorId(1L);
        doctor.setDoctorName("Dr. John Doe");

        doctorDTO = new DoctorDTO();
        doctorDTO.setDoctorName("Dr. John Doe");

        doctorResponseDTO = new DoctorResponseDTO();
        doctorResponseDTO.setDoctorId(1L);
        doctorResponseDTO.setDoctorName("Dr. John Doe");

        availabilityDTO = new DoctorAvailabilityDTO();
        availabilityDTO.setDoctorId(1L);
        availabilityDTO.setAvailableDate(LocalDate.now().plusDays(1));
        availabilityDTO.setStartTime(LocalTime.of(9, 0));
        availabilityDTO.setEndTime(LocalTime.of(17, 0));

        availability = new DoctorAvailability();
        availability.setAvailabilityId(101L);
        availability.setDoctor(doctor);
        availability.setAvailableDate(availabilityDTO.getAvailableDate());
        availability.setStartTime(availabilityDTO.getStartTime());
        availability.setEndTime(availabilityDTO.getEndTime());

        availabilityResponseDTO = new DoctorAvailabilityResponseDTO();
        availabilityResponseDTO.setAvailabilityId(101L);
        availabilityResponseDTO.setDoctorId(1L);
        availabilityResponseDTO.setAvailableDate(availabilityDTO.getAvailableDate());
        availabilityResponseDTO.setStartTime(availabilityDTO.getStartTime());
        availabilityResponseDTO.setEndTime(availabilityDTO.getEndTime());
    }

    @Test
    void testCreateDoctor_Success() {
        when(modelMapper.map(doctorDTO, Doctor.class)).thenReturn(doctor);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);
        when(modelMapper.map(doctor, DoctorResponseDTO.class)).thenReturn(doctorResponseDTO);

        DoctorResponseDTO result = doctorService.createDoctor(doctorDTO);

        assertNotNull(result);
        assertEquals(doctorResponseDTO.getDoctorId(), result.getDoctorId());
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void testGetDoctorById_Success() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(modelMapper.map(doctor, DoctorResponseDTO.class)).thenReturn(doctorResponseDTO);

        DoctorResponseDTO result = doctorService.getDoctorById(1L);

        assertNotNull(result);
        assertEquals(doctorResponseDTO.getDoctorId(), result.getDoctorId());
        verify(doctorRepository, times(1)).findById(1L);
    }

    @Test
    void testGetDoctorById_NotFound() {
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> doctorService.getDoctorById(99L));
    }

    @Test
    void testGetAllDoctor_Success() {
        when(doctorRepository.findAll()).thenReturn(Collections.singletonList(doctor));
        when(modelMapper.map(doctor, DoctorResponseDTO.class)).thenReturn(doctorResponseDTO);

        List<DoctorResponseDTO> result = doctorService.getAllDoctor();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    void testAddAvailability_Success() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(modelMapper.map(availabilityDTO, DoctorAvailability.class)).thenReturn(availability);
        when(availabilityRepository.save(any(DoctorAvailability.class))).thenReturn(availability);
        when(modelMapper.map(availability, DoctorAvailabilityResponseDTO.class)).thenReturn(availabilityResponseDTO);

        DoctorAvailabilityResponseDTO result = doctorService.addAvailability(1L, availabilityDTO);

        assertNotNull(result);
        assertEquals(availabilityResponseDTO.getAvailabilityId(), result.getAvailabilityId());
        verify(doctorRepository, times(1)).findById(1L);
        verify(availabilityRepository, times(1)).save(any(DoctorAvailability.class));
    }

    @Test
    void testAddAvailability_DoctorNotFound() {
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> doctorService.addAvailability(99L, availabilityDTO));
        verify(availabilityRepository, never()).save(any(DoctorAvailability.class));
    }
}