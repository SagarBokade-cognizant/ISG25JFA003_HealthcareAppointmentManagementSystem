package com.cognizant.hams.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    private String userId;
    private String username;
    private String password;

    @ManyToOne
    @JoinColumn(name = "roleId")
    private Role role;

    @PrePersist
    public void generateId() {
        if (this.userId == null) {
            this.userId = UUID.randomUUID().toString();
        }
    }
}