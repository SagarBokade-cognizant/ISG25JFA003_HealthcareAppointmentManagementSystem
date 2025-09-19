package com.cognizant.hams.controller;

import com.cognizant.hams.dto.AppointmentResponseDTO;
import com.cognizant.hams.dto.Request.DoctorAvailabilityDTO;
import com.cognizant.hams.dto.Response.DoctorAndAvailabilityResponseDTO;
import com.cognizant.hams.dto.Response.DoctorAvailabilityResponseDTO;
import com.cognizant.hams.dto.Request.DoctorDTO;
import com.cognizant.hams.dto.Response.DoctorResponseDTO;
import com.cognizant.hams.entity.Notification;
import com.cognizant.hams.service.DoctorService;
import com.cognizant.hams.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<DoctorResponseDTO> createDoctor(@Valid @RequestBody DoctorDTO doctorDTO){
        DoctorResponseDTO savedDoctor = doctorService.createDoctor(doctorDTO);
        return new ResponseEntity<>(savedDoctor, HttpStatus.CREATED);
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorResponseDTO> getDoctorById(@PathVariable("doctorId") Long doctorId){
        DoctorResponseDTO doctor = doctorService.getDoctorById(doctorId);
        return new ResponseEntity<>(doctor,HttpStatus.OK);

    }

    @GetMapping("/all")
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctor(){
        List<DoctorResponseDTO> doctors = doctorService.getAllDoctor();
        return new ResponseEntity<>(doctors,HttpStatus.OK);
    }

    @PutMapping("/{doctorId}")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(@PathVariable("doctorId") Long doctorId, @RequestBody DoctorDTO doctorDTO){
        DoctorResponseDTO updateDoctor = doctorService.updateDoctor(doctorId,doctorDTO);
        return new ResponseEntity<>(updateDoctor,HttpStatus.OK);

    }

    @DeleteMapping("/{doctorId}")
    public ResponseEntity<DoctorResponseDTO> deleteDoctor(@PathVariable("doctorId") Long doctorId){
        DoctorResponseDTO deleteDoctor = doctorService.deleteDoctor(doctorId);
        return new ResponseEntity<>(deleteDoctor, HttpStatus.OK);
    }

    @GetMapping("/specializations")
    public ResponseEntity<List<DoctorResponseDTO>> searchDoctorsBySpecialization(@RequestParam("specialization") String specialization){
        List<DoctorResponseDTO> doctors = doctorService.searchDoctorsBySpecialization(specialization);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping("/doctorName")
    public ResponseEntity<List<DoctorResponseDTO>> searchDoctorsByName(@RequestParam("name") String name){
        List<DoctorResponseDTO> doctors = doctorService.searchDoctorsByName(name);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @PostMapping("/{doctorId}/availability")
    public ResponseEntity<DoctorAvailabilityResponseDTO> addAvailability(@PathVariable("doctorId") Long doctorId, @Valid @RequestBody DoctorAvailabilityDTO slotDto) {
        DoctorAvailabilityResponseDTO savedSlot = doctorService.addAvailability(doctorId, slotDto);
        return new ResponseEntity<>(savedSlot, HttpStatus.CREATED);
    }

    @GetMapping("/{doctorId}/availability")
    public ResponseEntity<List<DoctorAvailabilityResponseDTO>> getAvailability(@PathVariable("doctorId") Long doctorId) {
        List<DoctorAvailabilityResponseDTO> availability = doctorService.getAvailability(doctorId);
        return new ResponseEntity<>(availability, HttpStatus.OK);
    }

    @PutMapping("/{doctorId}/availability/{availabilityId}")
    public ResponseEntity<DoctorAvailabilityResponseDTO> updateAvailabilitySlot(@PathVariable("doctorId") Long doctorId, @PathVariable("availabilityId") Long availabilityId, @RequestBody  DoctorAvailabilityDTO doctorAvailabilityDTO ) {
        DoctorAvailabilityResponseDTO doctorResponseDTO = doctorService.updateAvailabilitySlot(doctorId, availabilityId,doctorAvailabilityDTO);
        return new ResponseEntity<>(doctorResponseDTO,HttpStatus.OK);
    }

    @GetMapping("/doctor-availability")
    public ResponseEntity<List<DoctorAndAvailabilityResponseDTO>> getAvailableDoctor(@RequestParam("name") String doctorName){
        List<DoctorAndAvailabilityResponseDTO> doctorAndAvailabilityResponseDTOList = doctorService.getAvailableDoctor(doctorName);
        return new ResponseEntity<>(doctorAndAvailabilityResponseDTOList, HttpStatus.OK);
    }

    @GetMapping("/searchDoctor")
    public ResponseEntity<List<DoctorAndAvailabilityResponseDTO>> searchDoctorByName(@RequestParam("name") String doctorName){
        List<DoctorAndAvailabilityResponseDTO> doctorAndAvailabilityResponseDTOList = doctorService.searchDoctorByName(doctorName);
        return new ResponseEntity<>(doctorAndAvailabilityResponseDTOList, HttpStatus.OK);
    }
}
