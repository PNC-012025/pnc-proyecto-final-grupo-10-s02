package com.example.easybank.util.mapper;

import com.example.easybank.domain.dto.request.TransactionRequestDTO;
import com.example.easybank.domain.dto.response.TransactionResponseDTO;
import com.example.easybank.domain.entity.Transaction;

public class TransactionMapper {
    public static Transaction toEntity(TransactionRequestDTO transactionRequestDTO) {
        return Transaction.builder()
                .description(transactionRequestDTO.getDescription())
                .amount(transactionRequestDTO.getAmount())
                .build();
    }

    public static TransactionResponseDTO toDTO(Transaction transaction) {

        String type = transaction.getType() != null ? transaction.getType() : "UNKNOWN";

        String name;
        String accountNumber;

        if ("RECEIVER".equals(type)) {

            if (transaction.getOriginAccount() != null && transaction.getOriginAccount().getUser() != null) {
                name = transaction.getOriginAccount().getUser().getFirstName() + " " + transaction.getOriginAccount().getUser().getLastName();
                accountNumber = transaction.getOriginAccount().getNumber();
            } else {
                name = "Bank";
                accountNumber = "Unknown";
            }

        } else {

            if (transaction.getDestinationAccount() != null && transaction.getDestinationAccount().getUser() != null) {
                name = transaction.getDestinationAccount().getUser().getFirstName() + " " + transaction.getDestinationAccount().getUser().getLastName();
                accountNumber = transaction.getDestinationAccount().getNumber();
            } else {
                name = "Bank";
                accountNumber = "Unknown";
            }
        }

        return TransactionResponseDTO.builder()
                .id(transaction.getId())
                .name(name)
                .accountNumber(accountNumber)
                .amount(transaction.getAmount())
                .date(transaction.getDateTime())
                .description(transaction.getDescription())
                .type(type)
                .build();
    }

}

