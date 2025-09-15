package com.cognizant.hams.controller;

import com.cognizant.hams.dto.DoctorCreateDTO;
import com.cognizant.hams.dto.DoctorResponseDTO;
import com.cognizant.hams.dto.DoctorUpdateDTO;
import com.cognizant.hams.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService){
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<DoctorResponseDTO> createDoctor(@Valid @RequestBody DoctorCreateDTO doctorCreateDTO){
        DoctorResponseDTO savedDoctorDTO = doctorService.createDoctor(doctorCreateDTO);
        return new ResponseEntity<>(savedDoctorDTO,HttpStatus.CREATED);
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorResponseDTO> getDoctorByID(@PathVariable Long doctorId){
        DoctorResponseDTO doctorDetails = doctorService.getDoctorById(doctorId);
        return new ResponseEntity<>(doctorDetails, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors(){
        List<DoctorResponseDTO> doctorList = doctorService.getAllDoctors();
        return new ResponseEntity<>(doctorList,HttpStatus.OK);
    }

    @PutMapping(value = "/{doctorId}")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(@PathVariable("doctorId") Long doctorId,
                                                          @RequestBody DoctorUpdateDTO doctorUpdateDTO){
        DoctorResponseDTO updatedDoctorDTO = doctorService.updateDoctor(doctorId, doctorUpdateDTO);
        return new ResponseEntity<>(updatedDoctorDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{doctorId}")
    public ResponseEntity<DoctorResponseDTO> deleteDoctor(@PathVariable("doctorId") Long doctorId){
        DoctorResponseDTO deleteDoctorDTO = doctorService.deleteDoctor(doctorId);
        return new ResponseEntity<>(deleteDoctorDTO, HttpStatus.OK);
    }
}
