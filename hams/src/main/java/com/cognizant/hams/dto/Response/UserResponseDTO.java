package com.cognizant.hams.dto.Response;

import lombok.Data;
import com.cognizant.hams.entity.Role;

@Data
public class UserResponseDTO {
    private String userId;
    private String username;
    private Role role; // You might want to map this to a RoleResponseDTO
}