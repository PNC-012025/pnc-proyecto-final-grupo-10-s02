package com.example.easybank.application.service.implementation;

import com.example.easybank.application.dto.AccountCreateDTO;
import com.example.easybank.application.mapper.AccountMapper;
import com.example.easybank.application.service.AccountService;
import com.example.easybank.domain.entity.Account;
import com.example.easybank.domain.exception.AlreadyExistsException;
import com.example.easybank.domain.exception.ModelNotFoundException;
import com.example.easybank.infrastructure.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public void save(AccountCreateDTO accountCreate) throws Exception {
        accountRepository.findByNumber(accountCreate.getNumber())
                .ifPresent(account -> { throw new AlreadyExistsException("Account already exists"); });

        Account account = AccountMapper.toEntity(accountCreate);

        accountRepository.save(account);
    }

    @Override
    public Optional<Account> getAccountByNumber(String accountNumber) throws Exception {
        return Optional.empty();
    }
}
