package com.cognizant.hams.repository;

import com.cognizant.hams.entity.Appointment;
import com.cognizant.hams.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatient_PatientId(Long patientId);

    List<Appointment> findByDoctor_DoctorId(Long doctorId);

    List<Appointment> findByDoctorDoctorId(Long doctorId);

    List<Appointment> findByDoctorDoctorIdAndAppointmentDate(Long doctorId, LocalDate date);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.doctorId = :doctorId AND a.patient.patientId = :patientId")
    List<Appointment> findByDoctorIdAndPatientId(@Param("doctorId") Long doctorId,
                                                 @Param("patientId") Long patientId);

    long countByAppointmentDate(LocalDate date);
    long countByDoctor_DoctorIdAndStatusAndAppointmentDateGreaterThanEqual(
            Long doctorId,
            AppointmentStatus status,
            LocalDate date
    );

    List<Appointment> findByDoctor_DoctorIdAndAppointmentDate(Long doctorId, LocalDate now);
}
