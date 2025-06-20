package com.example.easybank.application.service.implementation;


import com.example.easybank.application.dto.response.UserResponseDTO;
import com.example.easybank.application.mapper.UserMapper;
import com.example.easybank.application.service.UserListService;
import com.example.easybank.domain.entity.Bill;
import com.example.easybank.domain.entity.Role;
import com.example.easybank.domain.entity.UserData;
import com.example.easybank.domain.exception.ModelNotFoundException;
import com.example.easybank.infrastructure.repository.RoleRepository;
import com.example.easybank.infrastructure.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserListServiceImpl implements UserListService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


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



}