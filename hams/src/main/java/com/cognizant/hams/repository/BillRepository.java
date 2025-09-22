package com.cognizant.hams.repository;


import com.cognizant.hams.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    Optional<Bill> findAllByAppointment_AppointmentId(Long appointmentId);

    Optional<Bill> findByAppointment_AppointmentId(Long appointmentId);
}