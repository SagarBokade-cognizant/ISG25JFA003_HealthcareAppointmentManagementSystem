package com.cognizant.hams.dto.Request;

import lombok.Data;

@Data
public class AdminUserRequestDTO {
    private String username;
    private String password;
    private String roleName;
}