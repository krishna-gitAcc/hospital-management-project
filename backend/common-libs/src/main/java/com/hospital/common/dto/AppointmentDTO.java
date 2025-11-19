package com.hospital.common.dto;

import com.hospital.common.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private UUID id;

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;

    @NotNull(message = "Appoint date time is required")
    private LocalDateTime appointmentDate;

    private AppointmentStatus appointmentStatus;

    private String reason;

    private String notes;

    @NotNull(message = "User ID of who booked (patient or receptionist)")
    private UUID bookedBy;
}
