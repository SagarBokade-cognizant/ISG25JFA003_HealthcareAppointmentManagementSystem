package com.cognizant.hams.controller;

import com.cognizant.hams.dto.Request.AdminUserRequestDTO;
import com.cognizant.hams.dto.Request.AuthRequest;
import com.cognizant.hams.dto.Response.AuthResponse;
import com.cognizant.hams.dto.Response.UserResponseDTO;
import com.cognizant.hams.entity.User;
import com.cognizant.hams.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AuthService authService;

    public AdminController(AuthService authService) {
        this.authService = authService;
    }

    // Only users with the ADMIN role can access this endpoint
    // AdminController.java

    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createPrivilegedUser(@RequestBody AdminUserRequestDTO request) {
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(request.getPassword());

        User createdUser = authService.createPrivilegedUser(newUser, request.getRoleName());

        // Create and return the response DTO
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setUserId(createdUser.getUserId());
        responseDTO.setUsername(createdUser.getUsername());
        responseDTO.setRole(createdUser.getRole());

        return ResponseEntity.ok(responseDTO);
    }
}