package com.hospital.common.dto;

import com.hospital.common.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private UUID id;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private UserRole userRole;

    private String name;

    private boolean active;
}
