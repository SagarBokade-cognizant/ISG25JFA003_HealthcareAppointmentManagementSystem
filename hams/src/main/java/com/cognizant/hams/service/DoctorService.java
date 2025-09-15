package com.cognizant.hams.service;

import com.cognizant.hams.dto.DoctorCreateDTO;
import com.cognizant.hams.dto.DoctorResponseDTO;
import com.cognizant.hams.dto.DoctorUpdateDTO;

import java.util.List;

public interface DoctorService {
    DoctorResponseDTO createDoctor(DoctorCreateDTO doctorCreateDTO);
    DoctorResponseDTO getDoctorById(Long id);
    List<DoctorResponseDTO> getAllDoctors();
    DoctorResponseDTO updateDoctor(Long id, DoctorUpdateDTO doctorUpdateDTO);
//    Only Admin can delete it
    DoctorResponseDTO deleteDoctor(Long doctorId);
    List<DoctorResponseDTO> searchDoctorsByName(String doctorName);
    List<DoctorResponseDTO> searchDoctorsBySpecialization(String specialization);
    List<DoctorResponseDTO> searchDoctorsByNameAndSpecialization(String name, String specialization);
}
