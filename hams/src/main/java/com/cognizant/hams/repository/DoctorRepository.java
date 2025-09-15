package com.cognizant.hams.repository;

import com.cognizant.hams.dto.DoctorResponseDTO;
import com.cognizant.hams.entity.Doctor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    boolean existsByEmail(@NotBlank(message = "Email is required") @Email(message = "Invalid Email") String email);

    boolean existsByDoctorNameAndSpecialization(@NotBlank(message = "Doctor name is required")
                                                @Size(min = 3, max = 25, message = "Name must be between 3 and 25 characters") String doctorName,
                                                @NotBlank(message = "Specialization of a doctor is required")
                                                @Size(max = 30, message = "Specialization must not exceed 30 characters") String specialization);


    List<Doctor> findByDoctorNameContainingIgnoreCase(String doctorName);
    List<Doctor> findBySpecializationContainingIgnoreCase(String specialization);

    List<Doctor> findByDoctorNameContainingIgnoreCaseAndSpecializationContainingIgnoreCase(String name, String specialization);
}
