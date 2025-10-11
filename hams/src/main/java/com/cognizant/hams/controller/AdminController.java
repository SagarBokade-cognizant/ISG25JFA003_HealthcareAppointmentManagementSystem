package com.cognizant.hams.controller;

import com.cognizant.hams.dto.request.AdminUserRequestDTO;
import com.cognizant.hams.dto.request.DoctorDTO;
import com.cognizant.hams.dto.response.DoctorResponseDTO;
import com.cognizant.hams.dto.response.PatientResponseDTO;
import com.cognizant.hams.dto.response.UserResponseDTO;
import com.cognizant.hams.entity.Doctor;
import com.cognizant.hams.service.impl.AuthServiceImpl;
import com.cognizant.hams.service.DoctorService;
import com.cognizant.hams.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthServiceImpl authService;
    private final PatientService patientService;
    private final DoctorService doctorService;


    // Only users with the ADMIN role can access this endpoint
    // AdminController.java

    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createPrivilegedUser(@RequestBody AdminUserRequestDTO request) {
        Doctor createdUser = authService.createPrivilegedUser(request);
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setDoctorId(createdUser.getDoctorId());
        responseDTO.setDoctorName(createdUser.getDoctorName());
        responseDTO.setEmail(createdUser.getEmail());
        responseDTO.setClinicAddress(createdUser.getClinicAddress());
        responseDTO.setQualification(createdUser.getQualification());
        responseDTO.setSpecialization(createdUser.getSpecialization());
        responseDTO.setContactNumber(createdUser.getContactNumber());
        responseDTO.setYearOfExperience(createdUser.getYearOfExperience());


        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/patients/{patientId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PatientResponseDTO> deletePatient(@PathVariable("patientId") Long patientId){
        PatientResponseDTO deletePatientDTO = patientService.deletePatient(patientId);
        return new ResponseEntity<>(deletePatientDTO, HttpStatus.OK);
    }

    @PatchMapping("/doctors/{doctorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(@PathVariable("doctorId") Long doctorId, @RequestBody DoctorDTO doctorDTO){
        DoctorResponseDTO updateDoctor = doctorService.updateDoctor(doctorId,doctorDTO);
        return new ResponseEntity<>(updateDoctor,HttpStatus.OK);
    }

//    ADMIN DELETE METHOD

    @DeleteMapping("/doctors/{doctorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDTO> deleteDoctor(@PathVariable("doctorId") Long doctorId){
        DoctorResponseDTO deleteDoctor = doctorService.deleteDoctor(doctorId);
        return new ResponseEntity<>(deleteDoctor, HttpStatus.OK);
    }
}