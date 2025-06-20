package com.example.easybank.application.service.implementation;


import com.example.easybank.application.dto.response.AccountResponseDTO;
import com.example.easybank.application.dto.response.BillResponseDTO;
import com.example.easybank.application.dto.response.TransactionResponseDTO;
import com.example.easybank.application.dto.response.UserResponseDTO;
import com.example.easybank.application.mapper.AccountMapper;
import com.example.easybank.application.mapper.BillMapper;
import com.example.easybank.application.mapper.TransactionMapper;
import com.example.easybank.application.mapper.UserMapper;
import com.example.easybank.application.service.UserListService;
import com.example.easybank.domain.entity.Bill;
import com.example.easybank.domain.entity.Role;
import com.example.easybank.domain.entity.Transaction;
import com.example.easybank.domain.entity.UserData;
import com.example.easybank.domain.exception.ModelNotFoundException;
import com.example.easybank.infrastructure.repository.BillRepository;
import com.example.easybank.infrastructure.repository.RoleRepository;
import com.example.easybank.infrastructure.repository.TransactionRepository;
import com.example.easybank.infrastructure.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
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


    @Override
    public List<UserResponseDTO> findAllUsers() throws Exception {
        return UserMapper.toDTOList(userRepository.findAll());
    }

    @Override
    public void delete(UUID id) throws Exception {

        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void changeRoles(UUID id, List<String> roles) {
        UserData user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Set<Role> newRoles = roles.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new EntityNotFoundException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(newRoles);

        userRepository.save(user);

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
    public List<AccountResponseDTO> getUserAccounts(UUID userId) {
        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return user.getAccounts().stream()
                .map(AccountMapper::toDTO)
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









}