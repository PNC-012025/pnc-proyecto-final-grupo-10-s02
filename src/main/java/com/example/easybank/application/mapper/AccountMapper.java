package com.example.easybank.application.mapper;

import com.example.easybank.application.dto.AccountCreateDTO;
import com.example.easybank.application.dto.response.AccountResponseDTO;
import com.example.easybank.domain.entity.Account;

import java.time.LocalDateTime;

public class AccountMapper {
    public static Account toEntity(AccountCreateDTO accountCreateDTO) {
        return Account.builder()
                .number(accountCreateDTO.getNumber())
                .type(accountCreateDTO.getType())
                .currency(accountCreateDTO.getCurrency())
                .balance(accountCreateDTO.getBalance())
                .createdAt(LocalDateTime.now())
                .user(accountCreateDTO.getUserData())
                .build();
    }

    public static AccountResponseDTO toDTO(Account account) {
            return AccountResponseDTO.builder()
                    .firstName(account.getUser().getFirstName())
                    .lastName(account.getUser().getLastName())
                    .accountNumber(account.getNumber())
                    .accountNumber(account.getNumber())

                    .build();

    }
}
