package com.cognizant.hams.controller;

import com.cognizant.hams.dto.request.AdminUserRequestDTO;
import com.cognizant.hams.dto.response.DoctorResponseDTO;
import com.cognizant.hams.entity.Doctor;
import com.cognizant.hams.service.AuthService;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin") // Assuming this is an admin-specific controller
public class AdminController {

    private final AuthService authService;
    private final ModelMapper modelMapper;

    // Constructor Injection (Recommended)
    public AdminController(AuthService authService, ModelMapper modelMapper) {
        this.authService = authService;
        this.modelMapper = modelMapper;
    }

    /**
     * Creates a new Doctor user (privileged user) based on the request.
     * Requires the ADMIN role.
     * @param request The DTO containing user credentials and doctor profile details.
     * @return A ResponseEntity containing the created doctor's profile details.
     */
    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDTO> createPrivilegedUser(@RequestBody AdminUserRequestDTO request) {
        // 1. Service call to create the User and associated Doctor entity
        Doctor createdDoctor = authService.createPrivilegedUser(request);

        // 2. Use ModelMapper to automatically convert the Doctor entity to the DoctorResponseDTO
        DoctorResponseDTO responseDTO = modelMapper.map(createdDoctor, DoctorResponseDTO.class);

        // Best practice: return 201 Created for a resource creation
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}
