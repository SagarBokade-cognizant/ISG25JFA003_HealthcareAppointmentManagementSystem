package com.cognizant.hams.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(mappedBy = "doctor")
    @JsonManagedReference
    private List<Appointment> appointments;

    @NotBlank(message = "Doctor name is required")
    @Size(min = 3, max = 25, message = "Name must be between 3 and 25 characters")
    private String doctorName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String contactNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email")
    private String email;

    @NotBlank(message = "Specialization of a doctor is required")
    @Size(max = 30, message = "Specialization must not exceed 30 characters")
    private String specialization;

    @NotNull(message = "Year of experience is required")
    private Integer yearOfExperience;

    @NotBlank(message = "Medical Qualification is required")
    @Size(max = 60, message = "Qualification should not exceed 100 characters")
    private String qualification;

    @NotBlank(message = "Clinic address is required")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String clinicAddress;


}


