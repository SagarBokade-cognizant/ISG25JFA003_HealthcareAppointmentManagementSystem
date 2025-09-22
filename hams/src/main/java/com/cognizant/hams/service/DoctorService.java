package com.cognizant.hams.service;

import com.cognizant.hams.dto.AppointmentResponseDTO;
import com.cognizant.hams.dto.Request.DoctorAvailabilityDTO;
import com.cognizant.hams.dto.Response.DoctorAndAvailabilityResponseDTO;
import com.cognizant.hams.dto.Response.DoctorAvailabilityResponseDTO;
import com.cognizant.hams.dto.Request.DoctorDTO;
import com.cognizant.hams.dto.Response.DoctorResponseDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface DoctorService {

    // Basic Curd operation
    DoctorResponseDTO createDoctor(DoctorDTO doctorDto);
    DoctorResponseDTO getDoctorById(Long doctorId);
    List<DoctorResponseDTO> getAllDoctor();
    DoctorResponseDTO updateDoctor(Long doctorId,DoctorDTO doctorDto);
    DoctorResponseDTO deleteDoctor(Long doctorId);


    // Domain Specific
    List<DoctorResponseDTO> searchDoctorsBySpecialization(String specialization);
    List<DoctorResponseDTO> searchDoctorsByName(String name);

    //
//    // Availability Management
    DoctorAvailabilityResponseDTO addAvailability(Long doctorId, DoctorAvailabilityDTO slotDto);
    List<DoctorAvailabilityResponseDTO> getAvailability(Long doctorId);
    DoctorAvailabilityResponseDTO updateAvailabilitySlot(Long doctorId,Long availabilityId, DoctorAvailabilityDTO doctorAvailabilityDTO);

    List<DoctorAndAvailabilityResponseDTO> getAvailableDoctor(String doctorName);

    List<DoctorAndAvailabilityResponseDTO> searchDoctorByName(String doctorName);


}
