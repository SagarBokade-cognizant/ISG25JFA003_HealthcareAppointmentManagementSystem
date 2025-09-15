package com.cognizant.hams.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorUpdateDTO {
    @NotBlank(message = "Doctor name is required")
    private String doctorName;

    @NotBlank(message = "Qualification is required")
    private String qualification;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotBlank(message = "Available days are required")
    private String availableDays;

    @NotBlank(message = "Clinic address is required")
    private String clinicAddress;

    @NotNull(message = "Year of experience is required")
    private Integer yearOfExperience;

    @Pattern(regexp = "\\d{10}", message = "Contact number must be 10 digits")
    private String contactNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email")
    private String email;
}
