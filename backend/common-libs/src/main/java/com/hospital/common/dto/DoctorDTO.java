package com.hospital.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDTO {
    private UUID id;

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    private String contact;

    private String Qualification;

    private Integer experience;

    private LocalTime availableFrom;

    private LocalTime availableTo;

    private List<String> availableDays;

    private Double consultationFee;
}
