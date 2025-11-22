package com.example.easybank.service.implementation;


import com.example.easybank.domain.dto.response.*;
import com.example.easybank.domain.mapper.*;
import com.example.easybank.exception.*;
import com.example.easybank.repository.AccountRepository;
import com.example.easybank.repository.RoleRepository;
import com.example.easybank.repository.TransactionRepository;
import com.example.easybank.repository.UserRepository;
import com.example.easybank.service.AdminService;
import com.example.easybank.domain.entity.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private UserData getUserEntityById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAllUsers() {
        return UserMapper.toDTOList(userRepository.findAll());
    }

    @Override
    public void delete(UUID id) {
        UserData user = getUserEntityById(id);

        userRepository.delete(user);
    }

    @Override
    public void changeRoles(UUID id, List<String> roles) {
        UserData user = getUserEntityById(id);

        Set<Role> newRoles = roles.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RoleNotFoundException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(newRoles);


        userRepository.save(user);


    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(UUID id) {
        UserData user = getUserEntityById(id);
        UserResponseDTO dto = UserMapper.toDTO(user);
        dto.setRoles(user.getRoles().stream().map(Role::getName).toList());
        return dto;
    }


    @Override
    @Transactional(readOnly = true)
    public List<AccountResponseAdminDTO> getUserAccounts(UUID userId) {
        UserData user = getUserEntityById(userId);

        return user.getAccounts().stream()
                .map(AccountMapper::toAdminDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillResponseDTO> getUserBills(UUID userId) {
        UserData user = getUserEntityById(userId);

        return user.getBills().stream()
                .map(BillMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminTransactionResponseDTO> getUserTransactions(String id) {

        //No ID provided
        if (id == null) {
            return transactionRepository.findAll()
                    .stream()
                    .map(AdminTransactionMapper::toDTO)
                    .toList();
        }

        // CASE 2 → Validate UUID
        UUID userId;
        try {
            userId = UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid UUID format: " + id);
        }

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        List<Transaction> transactions =
                transactionRepository.findByOriginAccount_User_IdOrDestinationAccount_User_IdOrderByDateTimeDesc(
                        userId, userId
                );

        return transactions.stream()
                .map(AdminTransactionMapper::toDTO)
                .toList();
    }


    @Override
    @Transactional
    public void depositToUserAccount(UUID userId, UUID accountId, BigDecimal amount, String description) {

        String username =  SecurityContextHolder.getContext().getAuthentication().getName();

        UserData admin = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Admin user not found with name: " + username));

        UserData user = getUserEntityById(userId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));

        if (!account.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Account does not belong to user");
        }

        if (admin.getAccounts().isEmpty()) {
            throw new IllegalArgumentException("Admin does not have an account to use as origin");
        }

        Account adminAccount = admin.getAccounts().getFirst();


        Transaction depositTx = Transaction.builder()
                .originAccount(adminAccount)
                .destinationAccount(account)
                .amount(amount)
                .type("DEPOSIT")
                .description(description)
                .dateTime(LocalDateTime.now())
                .build();


        transactionRepository.save(depositTx);


        account.setBalance(account.getBalance().add(amount));


        accountRepository.save(account);

    }
}