package com.example.easybank.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillRequestDTO {
    private String expenseName;
    private String category;
    private BigDecimal amount;
    private Date date;
}
