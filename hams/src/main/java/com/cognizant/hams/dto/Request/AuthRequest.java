package com.cognizant.hams.dto.Request;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
    private String roleName; // Changed from roleId
}