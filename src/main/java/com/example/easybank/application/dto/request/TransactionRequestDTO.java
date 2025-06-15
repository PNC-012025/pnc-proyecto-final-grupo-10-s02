package com.example.easybank.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequestDTO {
    private BigDecimal amount;
    private String type;
    private String currency;
    private String description;
    private String destinationAccountNumber;
}
