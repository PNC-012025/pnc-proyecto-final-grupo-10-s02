package com.example.easybank.application.mapper;

import com.example.easybank.application.dto.request.BillRequestDTO;
import com.example.easybank.application.dto.response.BillResponseDTO;
import com.example.easybank.domain.entity.Bill;

public class BillMapper {
    public static Bill toEntity(BillRequestDTO billRequestDTO) {
        return Bill.builder()
                .name(billRequestDTO.getName())
                .category(billRequestDTO.getCategory())
                .amount(billRequestDTO.getAmount())
                .date(billRequestDTO.getDate())
                .state(billRequestDTO.getState())
                .build();
    }

    public static BillResponseDTO toDTO(Bill bill) {
        return BillResponseDTO.builder()
                .name(bill.getName())
                .category(bill.getCategory())
                .amount(bill.getAmount())
                .date(bill.getDate())
                .state(bill.getState())
                .build();
    }
}
