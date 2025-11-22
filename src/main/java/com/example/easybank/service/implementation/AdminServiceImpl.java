package com.example.easybank.service.implementation;


import com.example.easybank.domain.dto.response.*;
import com.example.easybank.domain.mapper.*;
import com.example.easybank.exception.ModelNotFoundException;
import com.example.easybank.repository.AccountRepository;
import com.example.easybank.repository.RoleRepository;
import com.example.easybank.repository.TransactionRepository;
import com.example.easybank.repository.UserRepository;
import com.example.easybank.service.AdminService;
import com.example.easybank.domain.entity.*;
import jakarta.persistence.EntityNotFoundException;
import com.example.easybank.exception.StorageException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;


    @Override
    public List<UserResponseDTO> findAllUsers() {
        return UserMapper.toDTOList(userRepository.findAll());
    }

    @Override
    public void delete(UUID id) {
        UserData user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userRepository.delete(user);
    }

    @Override
    public void changeRoles(UUID id, List<String> roles) {
        UserData user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Set<Role> newRoles = roles.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new EntityNotFoundException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(newRoles);


        try{
            userRepository.save(user);
        }
        catch (DataAccessException e){
            throw new StorageException("Failed to update user");
        }

    }


    @Transactional//(readOnly = true)
    public UserResponseDTO getUserById(UUID id) {
        UserData user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        UserResponseDTO dto = UserMapper.toDTO(user);
        dto.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .toList());

        return dto;
    }

    @Override
    @Transactional//(readOnly = true)
    public List<AccountResponseAdminDTO> getUserAccounts(UUID userId) {
        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return user.getAccounts().stream()
                .map(AccountMapper::toAdminDTO)
                .toList();
    }

    @Override
    @Transactional//(readOnly = true)
    public List<BillResponseDTO> getUserBills(UUID userId) {
        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return user.getBills().stream()
                .map(BillMapper::toDTO)
                .toList();
    }

    public List<AdminTransactionResponseDTO> getUserTransactions(UUID userId, int limit, int page) {

        Pageable pageable = PageRequest.of(page, limit, Sort.by("dateTime").descending());

        if (userId == null) {
            // return ALL transactions
            return transactionRepository.findAll(pageable).stream()
                    .map(UserTransactionMapper::toDTO)
                    .toList();
        }

        // return one user's transactions
        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Page<Transaction> origin = transactionRepository.findByOriginAccount_User(user, pageable);
        Page<Transaction> destination = transactionRepository.findByDestinationAccount_User(user, pageable);

        List<Transaction> all = new ArrayList<>();
        all.addAll(origin.getContent());
        all.addAll(destination.getContent());

        all.sort(Comparator.comparing(Transaction::getDateTime).reversed());

        return all.stream()
                .map(UserTransactionMapper::toDTO)
                .toList();
    }


    @Override
    public List<AdminTransactionResponseDTO> findAll() throws Exception{
        List<Transaction> transactions = transactionRepository.findAll();

        return transactions.stream()
                .map(UserTransactionMapper::toDTO)
                .toList();
    }

    // easter egg

    @Override
    @Transactional
    public void depositToUserAccount(UUID userId, UUID accountId, BigDecimal amount, String description) {

        String username =  SecurityContextHolder.getContext().getAuthentication().getName();

        UserData admin = userRepository.findByUsername(username)
                .orElseThrow(() -> new ModelNotFoundException("User not found"));

        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        if (!account.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Account does not belong to user");
        }

        Transaction depositTx = Transaction.builder()
                .originAccount(null)
                .originAccount(admin.getAccounts().getFirst())
                .destinationAccount(account)
                .amount(amount)
                .type("DEPOSIT")
                .description(description)
                .dateTime(LocalDateTime.now())
                .build();

        try{
            transactionRepository.save(depositTx);
        }
        catch (DataAccessException e){
            throw new StorageException("Failed to save transaction");
        }

        account.setBalance(account.getBalance().add(amount));

        try{
            accountRepository.save(account);
        }
        catch (DataAccessException e){
            throw new StorageException("Failed to update account");
        }
    }
}