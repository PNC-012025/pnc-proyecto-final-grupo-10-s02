package com.example.easybank.service.implementation;

import com.example.easybank.domain.dto.request.TransactionRequestDTO;
import com.example.easybank.domain.dto.response.TransactionResponseDTO;
import com.example.easybank.domain.mapper.TransactionMapper;
import com.example.easybank.exception.*;
import com.example.easybank.repository.BillRepository;
import com.example.easybank.service.TransactionService;
import com.example.easybank.domain.entity.Account;
import com.example.easybank.domain.entity.Transaction;
import com.example.easybank.domain.entity.UserData;
import com.example.easybank.repository.AccountRepository;
import com.example.easybank.repository.TransactionRepository;
import com.example.easybank.repository.UserRepository;
import com.example.easybank.util.TransactionSpecifications;
import com.example.easybank.util.generator.RandomCreditCardGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final RandomCreditCardGenerator randomCreditCardGenerator;
    private final BillRepository billRepository;

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
        transaction.setType("TRANSFER");

        try{
            accountRepository.save(originAccount);
        }
        catch (DataAccessException e){
            throw new StorageException("Failed to update origin account");
        }

        try{
            accountRepository.save(destinationAccount);
        }
        catch (DataAccessException e){
            throw new StorageException("Failed to update destination account");
        }

        try{
            transactionRepository.save(transaction);
        }
        catch (DataAccessException e){
            throw new StorageException("Failed to save transaction");
        }

    }

    @Override
    public Page<TransactionResponseDTO> getAllTransactions(
            String type,
            LocalDateTime from,
            LocalDateTime to,
            String accountId,
            String originAccountId,
            String destinationAccountId,
            Pageable pageable
    ) throws Exception{
        Specification<Transaction> spec = Specification
                .allOf(
                        TransactionSpecifications.hasAccount(accountId),
                        TransactionSpecifications.hasType(type),
                        TransactionSpecifications.hasOriginAccount(originAccountId),
                        TransactionSpecifications.hasDestinationAccount(destinationAccountId),
                        TransactionSpecifications.betweenDates(from, to)
                );

        return transactionRepository.findAll(spec, pageable).map(TransactionMapper::toDTO);
    }
}
