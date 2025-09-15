package com.cognizant.hams.controller;

import com.cognizant.hams.dto.DoctorResponseDTO;
import com.cognizant.hams.dto.PatientCreatedDTO;
import com.cognizant.hams.dto.PatientResponseDTO;
import com.cognizant.hams.dto.PatientUpdateDTO;
import com.cognizant.hams.service.PatientService;
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

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@Valid @RequestBody PatientCreatedDTO patientCreatedDTO){
        PatientResponseDTO createPatientDTO = patientService.createPatient(patientCreatedDTO);
        return new ResponseEntity<>(createPatientDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable("patientId") Long patientId) {
        PatientResponseDTO getPatientDTO = patientService.getPatientById(patientId);
        return new ResponseEntity<>(getPatientDTO, HttpStatus.OK);
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable("patientId") Long patientId,
                                                          @Valid @RequestBody PatientUpdateDTO patientUpdateDTO) {
        PatientResponseDTO existingPatientDTO = patientService.updatePatient(patientId, patientUpdateDTO);
        return new ResponseEntity<>(existingPatientDTO,HttpStatus.OK);
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<PatientResponseDTO> deletePatient(@PathVariable("patientId") Long patientId) {
        PatientResponseDTO deletePatientDTO = patientService.deletePatient(patientId);
        return new ResponseEntity<>(deletePatientDTO, HttpStatus.OK);
    }

    @GetMapping("/nameSearch")
    public ResponseEntity<List<DoctorResponseDTO>> searchDoctorsByName(@RequestParam("name") String name){
         List<DoctorResponseDTO> doctorResponseDTO = patientService.searchDoctorByName(name);
         return new ResponseEntity<>(doctorResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/specializationSearch")
    public ResponseEntity<List<DoctorResponseDTO>> searchDoctorBySpecialization(@RequestParam("specialization") String specialization){
        List<DoctorResponseDTO> doctorResponseDTO = patientService.searchDoctorBySpecialization(specialization);
        return new ResponseEntity<>(doctorResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/doctorSearch")
    public ResponseEntity<List<DoctorResponseDTO>> searchDoctors(@RequestParam(value = "name") String name,
                                                                 @RequestParam(value = "specialization") String specialization){
        List<DoctorResponseDTO> doctorResponseDTOS = patientService.findAllDoctorsByPatient();
        return new ResponseEntity<>(doctorResponseDTOS, HttpStatus.OK);
    }
}
