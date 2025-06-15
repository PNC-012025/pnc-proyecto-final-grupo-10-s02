package com.example.easybank.application.service.implementation;

import com.example.easybank.application.dto.AccountCreateDTO;
import com.example.easybank.application.dto.response.AccountResponseDTO;
import com.example.easybank.application.mapper.AccountMapper;
import com.example.easybank.application.service.AccountService;
import com.example.easybank.domain.entity.Account;
import com.example.easybank.domain.entity.UserData;
import com.example.easybank.domain.exception.AlreadyExistsException;
import com.example.easybank.domain.exception.ModelNotFoundException;
import com.example.easybank.infrastructure.repository.AccountRepository;
import com.example.easybank.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

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

    @Override
    public AccountResponseDTO getMyOwnAccount() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserData user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ModelNotFoundException("User not found"));

        Account account = user.getAccounts().getFirst();

        return AccountResponseDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .accountNumber(account.getNumber())
                .balance(account.getBalance())
                .build();
    }
}
