package com.cognizant.hams.service.Impl;

import com.cognizant.hams.dto.AppointmentResponseDTO;
import com.cognizant.hams.dto.Request.DoctorAvailabilityDTO;
import com.cognizant.hams.dto.Response.DoctorAndAvailabilityResponseDTO;
import com.cognizant.hams.dto.Response.DoctorAvailabilityResponseDTO;
import com.cognizant.hams.dto.Request.DoctorDTO;
import com.cognizant.hams.dto.Response.DoctorResponseDTO;
import com.cognizant.hams.entity.Appointment;
import com.cognizant.hams.entity.AppointmentStatus;
import com.cognizant.hams.entity.Doctor;
import com.cognizant.hams.entity.DoctorAvailability;
import com.cognizant.hams.exception.APIException;
import com.cognizant.hams.exception.ResourceNotFoundException;
import com.cognizant.hams.repository.AppointmentRepository;
import com.cognizant.hams.repository.DoctorAvailabilityRepository;
import com.cognizant.hams.repository.DoctorRepository;
import com.cognizant.hams.service.DoctorService;
import com.cognizant.hams.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    private final DoctorAvailabilityRepository doctorAvailabilityRepository;

    private final ModelMapper modelMapper;

    private final AppointmentRepository appointmentRepository;

    private final NotificationService notificationService;

//    private SpecializationRepository specializationRepository;

//    public DoctorServiceImpl(DoctorRepository doctorRepository, SpecializationRepository specializationRepository){
//        this.doctorRepository = doctorRepository;
//        this.specializationRepository = specializationRepository;
//    }

    // Create Doctor

    @Override
    public DoctorResponseDTO createDoctor(DoctorDTO doctorDTO) {
        // Option A: If you're creating a new doctor, make sure the ID is null
        // and Hibernate will insert it.
        // If an ID is present, treat it as an update.
        Doctor doctor = modelMapper.map(doctorDTO,Doctor.class);
        if(doctorRepository.existsByDoctorNameAndSpecialization(doctor.getDoctorName(),doctor.getSpecialization())){
            throw new APIException("Doctor with name " + doctorDTO.getDoctorName() + " and specialization " + doctorDTO.getSpecialization() + " already exist.");
        }
        Doctor saveDoctor = doctorRepository.save(doctor);
        return modelMapper.map(saveDoctor,DoctorResponseDTO.class);
    }

    // Get Doctor By I'd:

    @Override
    public DoctorResponseDTO getDoctorById(Long doctorId) {
        Optional<Doctor> doctorOptional = doctorRepository.findByDoctorId(doctorId);
        if(doctorOptional.isEmpty()){
            throw new APIException("Doctor with doctorId " + doctorId + " does not exist.");
        }
        return modelMapper.map(doctorOptional,DoctorResponseDTO.class);
    }

    // Get All Doctor

    @Override
    public List<DoctorResponseDTO> getAllDoctor(){
        List<Doctor> doctors = doctorRepository.findAll();
        if(doctors.isEmpty()){
            throw new APIException("No Doctor Available");
        }
        return doctors.stream()
                .map(doctor -> modelMapper.map(doctor,DoctorResponseDTO.class))
                .collect(Collectors.toList());
    }

    // Update Doctor

    @Override
    public DoctorResponseDTO updateDoctor(Long doctorId, DoctorDTO doctorDTO) {
        Doctor existingDoctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "doctorId", doctorId));

        if(doctorDTO.getDoctorName() != null){
            existingDoctor.setDoctorName(doctorDTO.getDoctorName());
        }
        if(doctorDTO.getQualification() != null){
            existingDoctor.setQualification(doctorDTO.getQualification());
        }
        if(doctorDTO.getSpecialization() != null){
            existingDoctor.setSpecialization(doctorDTO.getSpecialization());
        }
        if(doctorDTO.getYearOfExperience() != null){
            existingDoctor.setYearOfExperience(doctorDTO.getYearOfExperience());
        }
        if(doctorDTO.getClinicAddress() != null){
            existingDoctor.setClinicAddress(doctorDTO.getClinicAddress());
        }
        if(doctorDTO.getEmail() != null){
            existingDoctor.setEmail(doctorDTO.getEmail());
        }

        doctorRepository.save(existingDoctor);
        return modelMapper.map(existingDoctor,DoctorResponseDTO.class);
    }

    // Delete Doctor

    @Override
    public DoctorResponseDTO deleteDoctor(Long doctorId){
        Doctor existingDoctor = doctorRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor","doctorId", doctorId));
        doctorRepository.deleteById(doctorId);
        return modelMapper.map(existingDoctor,DoctorResponseDTO.class);
    }

    // Search Doctors By Specialization

    @Override
    public List<DoctorResponseDTO> searchDoctorsBySpecialization(String specialization) {
        List<Doctor> doctorSpecialization = doctorRepository.findBySpecializationContainingIgnoreCase(specialization);
        if(doctorSpecialization == null){
            throw new ResourceNotFoundException("Doctor","Specialization",specialization);
        }

        return doctorSpecialization.stream()
                .map(doctor -> modelMapper.map(doctor,DoctorResponseDTO.class))
                .collect(Collectors.toList());
    }

    // Search Doctors By Name

    @Override
    public List<DoctorResponseDTO> searchDoctorsByName(String name) {
        List<Doctor> doctorName = doctorRepository.findByDoctorNameContainingIgnoreCase(name);
        if(doctorName == null){
            throw new ResourceNotFoundException("Doctor","Name",name);
        }

        return doctorName.stream()
                .map(doctor -> modelMapper.map(doctor,DoctorResponseDTO.class))
                .collect(Collectors.toList());
    }

    //Doctor Availability CURD Operations:

    // Add Availability

    @Override
    @Transactional
    public DoctorAvailabilityResponseDTO addAvailability(Long doctorId, DoctorAvailabilityDTO slotDto) {
        Doctor doctor = doctorRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "doctorId", doctorId));

        DoctorAvailability doctorAvailability = modelMapper.map(slotDto, DoctorAvailability.class);
        doctorAvailability.setDoctor(doctor);

        DoctorAvailability savedAvailability = doctorAvailabilityRepository.save(doctorAvailability);
        return modelMapper.map(savedAvailability, DoctorAvailabilityResponseDTO.class);
    }

    // Get Availability

    @Override
    public List<DoctorAvailabilityResponseDTO> getAvailability(Long doctorId) {
        List<DoctorAvailability> availabilities = doctorAvailabilityRepository.findByDoctorDoctorId(doctorId);
        return availabilities.stream()
                .map(availability -> modelMapper.map(availability, DoctorAvailabilityResponseDTO.class))
                .collect(Collectors.toList());
    }

    // Delete Availability Slot

    @Override
    @Transactional
    public DoctorAvailabilityResponseDTO updateAvailabilitySlot(Long doctorId, Long availabilityId, DoctorAvailabilityDTO doctorAvailabilityDTO) {
        DoctorAvailability existingAvailability = doctorAvailabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "availabilityId", availabilityId));

        if(doctorAvailabilityDTO.getAvailableDate() != null){
            existingAvailability.setAvailableDate(doctorAvailabilityDTO.getAvailableDate());
        }
        if(doctorAvailabilityDTO.getStartTime() != null){
            existingAvailability.setStartTime(doctorAvailabilityDTO.getStartTime());
        }
        if(doctorAvailabilityDTO.getEndTime() != null){
            existingAvailability.setEndTime(doctorAvailabilityDTO.getEndTime());
        }
        if(doctorAvailabilityDTO.isAvailable()){
            existingAvailability.setAvailable(true);
        }
        System.out.println(existingAvailability);
        doctorAvailabilityRepository.save(existingAvailability);
        return modelMapper.map(existingAvailability, DoctorAvailabilityResponseDTO.class);
    }

//    @Override
//    @Transactional
//    public DoctorAvailabilityResponseDTO searchAvailabilityByDoctor(String doctorName){
//
//    }

    @Override
    public List<DoctorAndAvailabilityResponseDTO> getAvailableDoctor(String doctorName){
        return doctorRepository.findByAvailableDoctorNameAndAvailability(doctorName);
    }


    @Override
    public List<DoctorAndAvailabilityResponseDTO> searchDoctorByName(String doctorName){
        return doctorRepository.findByDoctorNameAndAvailability(doctorName);
    }



}