package com.cognizant.hams.controller;

import com.cognizant.hams.dto.request.AdminUserRequestDTO;
import com.cognizant.hams.dto.request.DoctorDTO;
import com.cognizant.hams.dto.response.DoctorResponseDTO;
import com.cognizant.hams.entity.Doctor;
import com.cognizant.hams.service.AuthService;

import com.cognizant.hams.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthService authService;
    private final ModelMapper modelMapper;
    private final DoctorService doctorService;


    /**
     * Creates a new Doctor user (privileged user) based on the request.
     * Requires the ADMIN role.
     * @param request The DTO containing user credentials and doctor profile details.
     * @return A ResponseEntity containing the created doctor's profile details.
     */
    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDTO> createPrivilegedUser(@RequestBody AdminUserRequestDTO request) {
        Doctor createdDoctor = authService.createPrivilegedUser(request);

        DoctorResponseDTO responseDTO = modelMapper.map(createdDoctor, DoctorResponseDTO.class);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/doctors/{doctorId}")
    @PreAuthorize("/hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(
            @PathVariable Long doctorId,
            @RequestBody DoctorDTO doctorDTO) {

        DoctorResponseDTO updatedDoctor = doctorService.updateDoctor(doctorId, doctorDTO);
        return new ResponseEntity<>(updatedDoctor, HttpStatus.OK);
    }

    @DeleteMapping("/{doctorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDTO> deleteDoctor(@PathVariable Long doctorId) {

        DoctorResponseDTO deletedDoctorDTO = doctorService.deleteDoctor(doctorId);
        return new ResponseEntity<>(deletedDoctorDTO, HttpStatus.NO_CONTENT);

    }
}
