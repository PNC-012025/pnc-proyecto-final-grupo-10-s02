package com.example.easybank.application.service;

import com.example.easybank.application.dto.AccountCreateDTO;
import com.example.easybank.application.dto.response.AccountResponseDTO;
import com.example.easybank.domain.entity.Account;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountService {
    public void save(AccountCreateDTO accountCreate) throws Exception;
    public Optional<Account> getAccountByNumber(String accountNumber) throws Exception;
    public AccountResponseDTO getMyOwnAccount() throws Exception;
}
