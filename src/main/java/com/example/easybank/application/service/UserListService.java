package com.example.easybank.application.service;

import com.example.easybank.application.dto.response.UserResponseDTO;

import java.util.List;

public interface UserListService {
    List<UserResponseDTO> findAllUsers();
}
