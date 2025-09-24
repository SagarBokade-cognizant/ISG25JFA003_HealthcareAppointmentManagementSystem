package com.cognizant.hams.controller;

import com.cognizant.hams.dto.Request.PatientDTO;
import com.cognizant.hams.dto.Response.PatientResponseDTO;
import com.cognizant.hams.dto.Response.DoctorResponseDTO;
import com.cognizant.hams.service.NotificationService;
import com.cognizant.hams.service.Impl.PatientServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/patients")
public class PatientController {


    private final PatientServiceImpl patientService;
    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@Valid @RequestBody PatientDTO createDTO){
        PatientResponseDTO newPatient = patientService.createPatient(createDTO);
        return new ResponseEntity<>(newPatient, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable("id") Long patientId) {
        return ResponseEntity.ok(patientService.getPatientById(patientId));
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable("patientId") Long patientId,
                                                            @RequestBody PatientDTO patientUpdateDTO){
        PatientResponseDTO existingPatientDTO = patientService.updatePatient(patientId, patientUpdateDTO);
        return new ResponseEntity<>(existingPatientDTO,HttpStatus.OK);
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<PatientResponseDTO> deletePatient(@PathVariable("patientId") Long patientId){
        PatientResponseDTO deletePatientDTO = patientService.deletePatient(patientId);
        return new ResponseEntity<>(deletePatientDTO, HttpStatus.OK);
    }

//    @GetMapping("/doctorNameAndSpecialization")
//    public ResponseEntity<List<DoctorResponseDTO>> searchDoctorByNameAndSpecialization(@RequestParam("name") String name,
//                                                           @RequestParam("specialization") String specialization){
//        List<DoctorResponseDTO> doctors = patientService.searchDoctorsByNameAndSpecialization(name, specialization);
//        return new ResponseEntity<>(doctors, HttpStatus.OK);
//    }

    @GetMapping("/doctorName")
    public ResponseEntity<List<DoctorResponseDTO>> searchDoctorByName(@RequestParam("name") String name){
        List<DoctorResponseDTO> doctors = patientService.searchDoctorByName(name);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping("/doctorSpecialization")
    public ResponseEntity<List<DoctorResponseDTO>> searchDoctorBySpecialization(@RequestParam("specialization") String specialization){
        List<DoctorResponseDTO> doctors = patientService.searchDoctorBySpecialization(specialization);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping("/allDoctors")
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors(){
        List<DoctorResponseDTO> doctors = patientService.getAllDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }
}


