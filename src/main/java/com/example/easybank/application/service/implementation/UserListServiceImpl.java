package com.example.easybank.application.service.implementation;


import com.example.easybank.application.dto.response.UserResponseDTO;
import com.example.easybank.application.mapper.UserMapper;
import com.example.easybank.application.service.UserListService;
import com.example.easybank.domain.entity.Bill;
import com.example.easybank.domain.entity.Role;
import com.example.easybank.domain.entity.UserData;
import com.example.easybank.domain.exception.ModelNotFoundException;
import com.example.easybank.infrastructure.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserListServiceImpl implements UserListService {
    private final UserRepository userRepository;


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

    }

}