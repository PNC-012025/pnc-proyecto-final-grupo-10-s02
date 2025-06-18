package com.example.easybank.application.service.implementation;


import com.example.easybank.application.dto.response.UserResponseDTO;
import com.example.easybank.application.mapper.UserMapper;
import com.example.easybank.application.service.UserListService;
import com.example.easybank.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserListServiceImpl implements UserListService {
    private final UserRepository userRepository;


    @Override
    public List<UserResponseDTO> findAllUsers() {
        return UserMapper.toDTOList(userRepository.findAll());
    }
}