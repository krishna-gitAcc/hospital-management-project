package com.hospital.common.dto;

import com.hospital.common.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private UUID id;

    @NotNull(message = "User ID is required")
    private UUID  userId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Age is required")
    private Integer age;

    private Gender gender;

    private String contact;

    private String address;

    private LocalDate dateOfBirth;

    private String BloodGroup;

    private String medicalHistory;
}
