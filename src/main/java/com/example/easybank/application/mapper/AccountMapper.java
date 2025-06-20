package com.example.easybank.application.mapper;

import com.example.easybank.application.dto.AccountCreateDTO;
import com.example.easybank.application.dto.response.AccountResponseAdminDTO;
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


    public static AccountResponseAdminDTO toAdminDTO(Account account) {
        return AccountResponseAdminDTO.builder()
                .id(account.getId())
                .firstName(account.getUser().getFirstName())
                .lastName(account.getUser().getLastName())
                .accountNumber(account.getNumber())
                .balance(account.getBalance())

                .build();

    }
}
