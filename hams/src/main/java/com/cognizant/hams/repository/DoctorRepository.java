package com.cognizant.hams.repository;

import com.cognizant.hams.dto.Response.AppointmentResponseDTO;
import com.cognizant.hams.dto.Response.DoctorAvailabilityResponseDTO;
import com.cognizant.hams.dto.Response.DoctorDetailsResponseDTO;
import com.cognizant.hams.entity.Doctor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Doctor findByDoctorName(@NotBlank @Size(min = 2, message = "Enter valid name") String doctorName);

    Optional<Doctor> findByDoctorId(@NotBlank Long doctorId);

    boolean existsByDoctorNameAndSpecialization(@NotBlank(message = "Doctor name is required")
                                                @Size(min = 3, max = 25, message = "Name must be between 3 and 25 characters") String doctorName,
                                                @NotBlank(message = "Specialization of a doctor is required")
                                                @Size(max = 30, message = "Specialization must not exceed 30 characters") String specialization);

    List<Doctor> findBySpecializationContainingIgnoreCase(String specialization);

    List<Doctor> findByDoctorNameContainingIgnoreCase(@NotBlank(message = "Doctor name is required")
                                                      @Size(min = 3, max = 25, message = "Name must be between 3 and 25 characters") String name);


    @Query(value = "select d.doctor_id, d.contact_number, d.doctor_name, d.email, d.clinic_address, d.specialization, d.qualification,\n" +
            "d.year_of_experience, a.start_time, a.end_time, a.available_date, a.available  from doctors d\n" +
            "join doctor_availability a\n" +
            "on d.doctor_id = a.doctor_id\n" +
            "where doctor_name = :doctorName and available=1;", nativeQuery = true)
    List<DoctorAvailabilityResponseDTO> findByAvailableDoctorNameAndAvailability(String doctorName);

    @Query(value = "select d.doctor_id, d.contact_number, d.doctor_name, d.email, d.clinic_address, d.specialization, d.qualification,\n" +
            "d.year_of_experience, a.start_time, a.end_time, a.available_date, a.available  from doctors d\n" +
            "join doctor_availability a\n" +
            "on d.doctor_id = a.doctor_id\n" +
            "where doctor_name = :doctorName", nativeQuery = true)
    List<DoctorAvailabilityResponseDTO> findByDoctorNameAndAvailability(String doctorName);

    @Query(value = "select a.appointment_id,d_doctor_name from appointments a\n" +
            "join doctors d\n" +
            "on a.doctor_id = d.doctor_id\n" +
            "where a.appointment_id = :appointmentId;", nativeQuery = true)
    List<AppointmentResponseDTO> findByAppointmentByAppointmentId(Long appointmentId);

}
