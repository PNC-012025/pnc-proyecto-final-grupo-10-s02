package com.example.easybank.application.service.implementation;


import com.example.easybank.application.dto.response.*;
import com.example.easybank.application.mapper.AccountMapper;
import com.example.easybank.application.mapper.BillMapper;
import com.example.easybank.application.mapper.TransactionMapper;
import com.example.easybank.application.mapper.UserMapper;
import com.example.easybank.application.service.UserListService;
import com.example.easybank.domain.entity.*;
import com.example.easybank.domain.exception.ModelNotFoundException;
import com.example.easybank.domain.exception.StorageException;
import com.example.easybank.infrastructure.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserListServiceImpl implements UserListService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;


    @Override
    public List<UserResponseDTO> findAllUsers() throws Exception {
        return UserMapper.toDTOList(userRepository.findAll());
    }

    @Override
    public void delete(UUID id) throws Exception {

        userRepository.deleteById(id);
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
        catch (Exception e){
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

    @Override
    @Transactional//(readOnly = true)
    public List<TransactionResponseDTO> getUserTransactions(UUID userId, int limit, int page) {

        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page, limit, Sort.by("dateTime").descending());

        Page<Transaction> pageResult = transactionRepository.findByOriginAccount_User(user, pageable);

        return pageResult.getContent().stream()
                .map(TransactionMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public void depositToUserAccount(UUID userId, UUID accountId, BigDecimal amount, String description) {

        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        if (!account.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Account does not belong to user");
        }

        Transaction depositTx = Transaction.builder()
                .originAccount(null)
                .destinationAccount(account)
                .amount(amount)
                .type("DEPOSIT")
                .description(description)
                .dateTime(LocalDateTime.now())
                .build();

        try{
            transactionRepository.save(depositTx);
        }
        catch (Exception e){
            throw new StorageException("Failed to save transaction");
        }


        account.setBalance(account.getBalance().add(amount));

        try{
            accountRepository.save(account);
        }
        catch (Exception e){
            throw new StorageException("Failed to update account");
        }
    }










}