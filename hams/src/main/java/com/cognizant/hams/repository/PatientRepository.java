package com.cognizant.hams.repository;

import com.cognizant.hams.entity.Patient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    boolean existsByEmailAndContactNumber(@Email @NotBlank String email, @Pattern(regexp = "\\d{10}", message = "Invalid contact number") String contactNumber);
}
