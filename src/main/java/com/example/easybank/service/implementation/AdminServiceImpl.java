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
import com.example.easybank.util.RoleName;
import lombok.RequiredArgsConstructor;
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

    private Role getRole(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException("Role not found: " + name));
    }

    private Account getAccountById(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + id));
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
    public void changeRoles(UUID id, List<RoleName> roles) {

        UserData user = getUserEntityById(id);

        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("User must have at least one role");
        }

        Set<Role> newRoles = roles.stream()
                .map(roleEnum -> getRole(roleEnum.name()))
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

        //Handle id vacio
        if (id == null || id.isBlank()) {
            return transactionRepository.findAll()
                    .stream()
                    .map(AdminTransactionMapper::toDTO)
                    .toList();
        }

        UUID userId;
        try {
            userId = UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            throw new InvalidUUIDException("Invalid UUID format: " + id);
        }

        getUserEntityById(userId);

        List<Transaction> transactions =
                transactionRepository
                        .findByOriginAccount_User_IdOrDestinationAccount_User_IdOrderByDateTimeDesc(
                                userId, userId);

        return transactions.stream()
                .map(AdminTransactionMapper::toDTO)
                .toList();
    }


    @Override
    @Transactional
    public void depositToUserAccount(UUID userId,
                                     UUID accountId,
                                     BigDecimal amount,
                                     String description,
                                     String performedByUsername) {

        // Get admin realizando la accion
        UserData admin = userRepository.findByUsername(performedByUsername)
                .orElseThrow(() -> new UserNotFoundException(
                        "Admin user not found with name: " + performedByUsername));

        UserData user = getUserEntityById(userId);
        Account account = getAccountById(accountId);

        if (!account.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Account does not belong to the specified user");
        }

        if (admin.getAccounts().isEmpty()) {
            throw new IllegalArgumentException("Admin does not have an account to use as origin");
        }

        //Validar cantidad a depositar
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be a positive number");
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
