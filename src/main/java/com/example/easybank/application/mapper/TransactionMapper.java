package com.example.easybank.application.mapper;

import com.example.easybank.application.dto.request.TransactionRequestDTO;
import com.example.easybank.application.dto.response.TransactionResponseDTO;
import com.example.easybank.domain.entity.Transaction;

public class TransactionMapper {
    public static Transaction toEntity(TransactionRequestDTO transactionRequestDTO) {
        return Transaction.builder()
                .description(transactionRequestDTO.getDescription())
                .amount(transactionRequestDTO.getAmount())
                .build();
    }

    public static TransactionResponseDTO toDTO(Transaction transaction) {
        if(transaction.getType().equals("RECEIVER")){
            return TransactionResponseDTO.builder()
                    .id(transaction.getId())
                    .name(transaction.getOriginAccount().getUser().getFirstName() + " " + transaction.getOriginAccount().getUser().getLastName())
                    .accountNumber(transaction.getOriginAccount().getNumber())
                    .amount(transaction.getAmount())
                    .date(transaction.getDateTime())
                    .description(transaction.getDescription())
                    .type(transaction.getType())
                    .build();
        }

        else{
            return TransactionResponseDTO.builder()
                    .id(transaction.getId())
                    .name(transaction.getDestinationAccount().getUser().getFirstName() + " " + transaction.getDestinationAccount().getUser().getLastName())
                    .accountNumber(transaction.getDestinationAccount().getNumber())
                    .amount(transaction.getAmount())
                    .date(transaction.getDateTime())
                    .description(transaction.getDescription())
                    .type(transaction.getType())
                    .build();

        }

    }
}
