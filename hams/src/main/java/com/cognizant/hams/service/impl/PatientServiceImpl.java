package com.cognizant.hams.service.impl;

import com.cognizant.hams.dto.DoctorResponseDTO;
import com.cognizant.hams.dto.PatientCreatedDTO;
import com.cognizant.hams.dto.PatientResponseDTO;
import com.cognizant.hams.dto.PatientUpdateDTO;
import com.cognizant.hams.entity.Patient;
import com.cognizant.hams.exception.APIException;
import com.cognizant.hams.exception.ResourceNotFoundException;
import com.cognizant.hams.repository.PatientRepository;
import com.cognizant.hams.service.DoctorService;
import com.cognizant.hams.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final ModelMapper modelMapper;

    @Override
    public PatientResponseDTO createPatient(PatientCreatedDTO patientCreatedDTO) {
        Patient patient = modelMapper.map(patientCreatedDTO, Patient.class);
        if(patientRepository.existsByEmailAndContactNumber(patient.getEmail(), patient.getContactNumber())){
            throw new APIException("Person with email " + patient.getEmail() + " and contact number " + patient.getContactNumber() + " already exist");
        }
        Patient savedPatient = patientRepository.save(patient);
        return modelMapper.map(savedPatient, PatientResponseDTO.class);
    }

    @Override
    public PatientResponseDTO getPatientById(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "patientId", patientId));
        return  modelMapper.map(patient, PatientResponseDTO.class);
    }

    @Override
    public PatientResponseDTO updatePatient(Long patientId, PatientUpdateDTO patientUpdateDTO) {
        Patient existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "patientId", patientId));
        if(patientUpdateDTO.getContactNumber() != null){
            existingPatient.setContactNumber(patientUpdateDTO.getContactNumber());
        }
        if(patientUpdateDTO.getEmail() != null){
            existingPatient.setEmail(patientUpdateDTO.getEmail());
        }
        if(patientUpdateDTO.getAddress() != null){
            existingPatient.setAddress(patientUpdateDTO.getAddress());
        }
        if(patientUpdateDTO.getGender() != null){
            existingPatient.setGender(patientUpdateDTO.getGender());
        }
        if(patientUpdateDTO.getName() != null){
            existingPatient.setName(patientUpdateDTO.getName());
        }
        if(patientUpdateDTO.getBloodGroup() != null){
            existingPatient.setBloodGroup(patientUpdateDTO.getBloodGroup());
        }
        if(patientUpdateDTO.getDateOfBirth() != null){
            existingPatient.setDateOfBirth(patientUpdateDTO.getDateOfBirth());
        }
        return modelMapper.map(existingPatient, PatientResponseDTO.class);
    }

    @Override
    public PatientResponseDTO deletePatient(Long patientId) {
        Patient deletePatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "patientId", patientId));
        patientRepository.delete(deletePatient);
        return modelMapper.map(deletePatient, PatientResponseDTO.class);
    }

    @Override
    public List<DoctorResponseDTO> findAllDoctorsByPatient() {
        return doctorService.getAllDoctors();
    }

    @Override
    public List<DoctorResponseDTO> searchDoctorByName(String name) {
        return doctorService.searchDoctorsByName(name);
    }

    @Override
    public List<DoctorResponseDTO> searchDoctorBySpecialization(String specialization) {
        return doctorService.searchDoctorsBySpecialization(specialization);
    }

    @Override
    public List<DoctorResponseDTO> searchDoctors(String name, String specialization) {
        return doctorService.searchDoctorsByNameAndSpecialization(name, specialization);
    }
}
