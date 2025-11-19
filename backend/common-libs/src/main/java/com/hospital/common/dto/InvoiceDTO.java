package com.hospital.common.dto;

import com.hospital.common.enums.InvoiceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    private UUID id;

    @NotNull(message = "Appointment ID is required")
    private UUID appointmentID;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    private InvoiceStatus invoiceStatus;

    private LocalDateTime issuedAt;

    private LocalDateTime paidAt;

    @NotNull(message = "User ID of who issued the invoice")
    private UUID issuedBy;
}
