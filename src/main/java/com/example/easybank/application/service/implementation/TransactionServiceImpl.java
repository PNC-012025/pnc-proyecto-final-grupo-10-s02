package com.example.easybank.application.service.implementation;

import com.example.easybank.application.dto.request.TransactionRequestDTO;
import com.example.easybank.application.dto.response.TransactionResponseDTO;
import com.example.easybank.application.mapper.TransactionMapper;
import com.example.easybank.application.service.TransactionService;
import com.example.easybank.domain.entity.Account;
import com.example.easybank.domain.entity.Transaction;
import com.example.easybank.domain.entity.UserData;
import com.example.easybank.domain.exception.*;
import com.example.easybank.infrastructure.repository.AccountRepository;
import com.example.easybank.infrastructure.repository.TransactionRepository;
import com.example.easybank.infrastructure.repository.UserRepository;
import com.example.easybank.util.generator.RandomCreditCardGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final RandomCreditCardGenerator randomCreditCardGenerator;

    @Override
    public void createTransaction(TransactionRequestDTO transactionRequestDTO) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(transactionRequestDTO);
        UserData user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ModelNotFoundException("User not found"));

        Account originAccount = accountRepository.findByNumber(user.getAccounts().getFirst().getNumber())
                .orElseThrow(() -> new ModelNotFoundException("Origin account not found"));

        Account destinationAccount = accountRepository.findByNumber(transactionRequestDTO.getAccountNumber())
                .orElseThrow(() -> new ModelNotFoundException("Destination account not found"));

        if (originAccount.getNumber().equals(destinationAccount.getNumber())) {
            throw new InvalidTransferException("Cannot transfer to the same account");
        }

        if (!destinationAccount.getUser().getFirstName().toLowerCase().equals(transactionRequestDTO.getFirstName())) {
            throw new RecipientNameMismatchException("Some recipients do not match");
        }

        if (!destinationAccount.getUser().getLastName().toLowerCase().equals(transactionRequestDTO.getLastName())) {
            throw new RecipientNameMismatchException("Some recipients do not match Last name");
        }

        BigDecimal amount = transactionRequestDTO.getAmount();
        BigDecimal originBalance = originAccount.getBalance();

        if (originBalance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient balance in the source account");
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

    @Override
    public List<TransactionResponseDTO> findMyOwnTransactions() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserData user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ModelNotFoundException("User not found"));

        Account account = user.getAccounts().getFirst();

        List<TransactionResponseDTO> transactionResponseOriginDTOS = account
                .getOriginTransactions().stream().map(transaction -> TransactionResponseDTO.builder()
                        .id(transaction.getId())
                        .accountNumber(account.getNumber())
                        .amount(transaction.getAmount())
                        .date(transaction.getDateTime())
                        .description(transaction.getDescription())
                        .type("SENDER")
                        .build()).toList();

        List<TransactionResponseDTO> transactionResponseDestinationDTOS = account
                .getDestinationTransactions().stream().map(transaction -> TransactionResponseDTO.builder()
                        .id(transaction.getId())
                        .accountNumber(account.getNumber())
                        .amount(transaction.getAmount())
                        .description(transaction.getDescription())
                        .date(transaction.getDateTime())
                        .type("RECEIVER")
                        .build()).toList();

        List<TransactionResponseDTO> finalResponse = new ArrayList<>();
        finalResponse.addAll(transactionResponseOriginDTOS);
        finalResponse.addAll(transactionResponseDestinationDTOS);

        return finalResponse;
    }
}
