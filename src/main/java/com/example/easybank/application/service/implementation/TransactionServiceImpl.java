package com.example.easybank.application.service.implementation;

import com.example.easybank.application.dto.request.TransactionRequestDTO;
import com.example.easybank.application.mapper.TransactionMapper;
import com.example.easybank.application.service.TransactionService;
import com.example.easybank.domain.entity.Account;
import com.example.easybank.domain.entity.Transaction;
import com.example.easybank.domain.entity.UserData;
import com.example.easybank.domain.exception.InvalidAmountException;
import com.example.easybank.domain.exception.ModelNotFoundException;
import com.example.easybank.infrastructure.repository.AccountRepository;
import com.example.easybank.infrastructure.repository.TransactionRepository;
import com.example.easybank.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Override
    public void createTransaction(TransactionRequestDTO transactionRequestDTO) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserData user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ModelNotFoundException("User not found"));

        Account originAccount = accountRepository.findByNumber(user.getAccounts().getFirst().getNumber())
                .orElseThrow(() -> new ModelNotFoundException("Origin account not found"));

        Account destinationAccount = accountRepository.findByNumber(transactionRequestDTO.getDestinationAccountNumber())
                .orElseThrow(() -> new ModelNotFoundException("Destination account not found"));

        // TODO: Change this exception
        if (originAccount.getNumber().equals(destinationAccount.getNumber())) {
            throw new InvalidAmountException("Cannot be transferred to the same account");
        }

        BigDecimal amount = transactionRequestDTO.getAmount();
        BigDecimal originBalance = originAccount.getBalance();

        if (originBalance.compareTo(amount) < 0) {
            throw new InvalidAmountException("Insufficient balance in the source account");
        }

        originAccount.setBalance(originBalance.subtract(amount));
        destinationAccount.setBalance(destinationAccount.getBalance().add(amount));

        Transaction transaction = TransactionMapper.toEntity(transactionRequestDTO);
        transaction.setOriginAccount(originAccount);
        transaction.setDestinationAccount(destinationAccount);
        transaction.setDateTime(LocalDateTime.now());

        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);
        transactionRepository.save(transaction);
    }

    
}
