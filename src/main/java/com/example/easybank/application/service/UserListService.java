package com.example.easybank.application.service;


import com.example.easybank.application.dto.response.UserResponseDTO;

import java.util.List;
import java.util.UUID;

public interface UserListService {
    List<UserResponseDTO> findAllUsers() throws Exception;
    void delete(UUID id) throws Exception;
    void changeRoles(UUID id, List<String> roles);
    UserResponseDTO getUserById(UUID id);


}
