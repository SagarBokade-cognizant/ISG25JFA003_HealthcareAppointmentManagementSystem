package com.cognizant.hams.service.impl;

import com.cognizant.hams.dto.DoctorCreateDTO;
import com.cognizant.hams.dto.DoctorResponseDTO;
import com.cognizant.hams.dto.DoctorUpdateDTO;
import com.cognizant.hams.entity.Doctor;
import com.cognizant.hams.exception.APIException;
import com.cognizant.hams.exception.ResourceNotFoundException;
import com.cognizant.hams.repository.DoctorRepository;
import com.cognizant.hams.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    @Override
    public DoctorResponseDTO createDoctor(DoctorCreateDTO doctorCreateDTO) {
        Doctor doctor = modelMapper.map(doctorCreateDTO, Doctor.class);
        if(doctorRepository.existsByDoctorNameAndSpecialization(doctor.getDoctorName(), doctor.getSpecialization())){
            throw new APIException("Doctor with same name and specialization already exists");
        }
        Doctor savedDoctor = doctorRepository.save(doctor);
        return modelMapper.map(savedDoctor,DoctorResponseDTO.class);
    }

    @Override
    public DoctorResponseDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new APIException("Doctor with doctorId " + id + " does not exist"));

        return modelMapper.map(doctor, DoctorResponseDTO.class);
    }

    @Override
    public List<DoctorResponseDTO> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        if(doctors.isEmpty()){
            throw  new APIException("Doctor not found");
        }
        return doctors.stream()
                .map(doctor -> modelMapper.map(doctor, DoctorResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public DoctorResponseDTO updateDoctor(Long doctorId, DoctorUpdateDTO doctorUpdateDTO) {
        Doctor existingDoctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "doctorId", doctorId));

        if(doctorUpdateDTO.getDoctorName() != null){
            existingDoctor.setDoctorName(doctorUpdateDTO.getDoctorName());
        }
        if(doctorUpdateDTO.getQualification() != null){
            existingDoctor.setQualification(doctorUpdateDTO.getQualification());
        }
        if(doctorUpdateDTO.getSpecialization() != null){
            existingDoctor.setSpecialization(doctorUpdateDTO.getSpecialization());
        }
        if(doctorUpdateDTO.getYearOfExperience() != null){
            existingDoctor.setYearOfExperience(doctorUpdateDTO.getYearOfExperience());
        }
        if(doctorUpdateDTO.getAvailableDays() != null){
            existingDoctor.setAvailableDays(doctorUpdateDTO.getAvailableDays());
        }
        if(doctorUpdateDTO.getClinicAddress() != null){
            existingDoctor.setClinicAddress(doctorUpdateDTO.getClinicAddress());
        }
        if(doctorUpdateDTO.getEmail() != null){
            existingDoctor.setEmail(doctorUpdateDTO.getEmail());
        }
        return modelMapper.map(existingDoctor,DoctorResponseDTO.class);
    }

//    Only admin can delete
    @Override
    public DoctorResponseDTO deleteDoctor(Long doctorId) {
        Doctor deleteDoctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "doctorId", doctorId));
        doctorRepository.delete(deleteDoctor);
        return modelMapper.map(deleteDoctor,DoctorResponseDTO.class);
    }

    @Override
    public List<DoctorResponseDTO> searchDoctorsByName(String doctorName) {
        List<Doctor> searchByDoctorsName = doctorRepository.findByDoctorNameContainingIgnoreCase(doctorName);
        if(searchByDoctorsName.isEmpty()){
            throw new APIException("Doctor with name " + doctorName + " does not exist");
        }
        return searchByDoctorsName.stream()
                .map(doctor -> modelMapper.map(doctor,DoctorResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponseDTO> searchDoctorsBySpecialization(String specialization) {
        List<Doctor> searchBySpecialization = doctorRepository.findBySpecializationContainingIgnoreCase(specialization);
        if(searchBySpecialization.isEmpty()){
            throw new APIException("Doctor with specialization " + specialization + " does not exist");
        }
        return searchBySpecialization.stream()
                .map(doctor -> modelMapper.map(doctor, DoctorResponseDTO.class))
                .collect(Collectors.toList());
    }

    public List<DoctorResponseDTO> searchDoctorsByNameAndSpecialization(String name, String specialization) {
        List<Doctor> doctors;

        if (name != null && !name.trim().isEmpty() && specialization != null && !specialization.trim().isEmpty()) {
            doctors = doctorRepository.findByDoctorNameContainingIgnoreCaseAndSpecializationContainingIgnoreCase(name, specialization);
        } else if (name != null && !name.trim().isEmpty()) {
            doctors = doctorRepository.findByDoctorNameContainingIgnoreCase(name);
        } else if (specialization != null && !specialization.trim().isEmpty()) {
            doctors = doctorRepository.findBySpecializationContainingIgnoreCase(specialization);
        } else {
            doctors = doctorRepository.findAll();
        }

        if (doctors.isEmpty()) {
            throw new APIException("No doctors found with the specified criteria.");
        }

        return doctors.stream()
                .map(doctor -> modelMapper.map(doctor, DoctorResponseDTO.class))
                .collect(Collectors.toList());
    }
}
