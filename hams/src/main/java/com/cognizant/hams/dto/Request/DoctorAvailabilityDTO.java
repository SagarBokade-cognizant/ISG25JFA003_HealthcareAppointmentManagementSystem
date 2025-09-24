package com.cognizant.hams.dto.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class DoctorAvailabilityDTO {

    private Long availabilityId;

    private Long doctorId;

    @FutureOrPresent(message = "Availability date must be in the present or future")
    @NotNull(message = "Availability date is required")
    @Column(name = "available_date", nullable = false)
    private LocalDate availableDate;

    @NotNull(message = "Start time is required")
    @JsonFormat(pattern = "HH:mm")
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    @JsonFormat(pattern = "HH:mm")
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    private boolean available;
}