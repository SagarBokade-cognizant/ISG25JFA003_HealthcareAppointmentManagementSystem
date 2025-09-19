package com.cognizant.hams.controller;

import com.cognizant.hams.dto.Request.AuthRequest;
import com.cognizant.hams.dto.Response.AuthResponse;
import com.cognizant.hams.entity.User;
import com.cognizant.hams.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authenticationRequest) {
        final String token = authService.createAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest registrationRequest) {
        User newUser = new User();
        newUser.setUsername(registrationRequest.getUsername());
        newUser.setPassword(registrationRequest.getPassword());

        User registeredUser = authService.registerNewUser(newUser, registrationRequest.getRoleName()); // Pass roleName here
        return ResponseEntity.ok("User registered successfully with ID: " + registeredUser.getUserId());
    }
}