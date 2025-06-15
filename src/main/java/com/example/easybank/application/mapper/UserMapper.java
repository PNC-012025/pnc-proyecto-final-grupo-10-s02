package com.example.easybank.application.mapper;

import com.example.easybank.application.dto.request.RegisterDTO;
import com.example.easybank.application.dto.response.UserResponseDTO;
import com.example.easybank.domain.entity.UserData;
import com.example.easybank.util.PasswordUtil;

public class UserMapper {
    public static UserData toEntity(RegisterDTO registerDTO) {
        return UserData.builder()
                .username(registerDTO.getUsername())
                .firstName(registerDTO.getFirstName())
                .lastName(registerDTO.getLastName())
                .dui(registerDTO.getDui())
                .email(registerDTO.getEmail())
                .hashedPassword(PasswordUtil.hashPassword(registerDTO.getPassword()))
                .active(true)
                .build();
    }

    public static UserResponseDTO toDTO(UserData userData) {
        return UserResponseDTO.builder()
                .id(userData.getId())
                .dui(userData.getDui())
                .username(userData.getUsername())
                .name(userData.getFirstName() + " " + userData.getLastName())
                .email(userData.getEmail())
                .active(userData.getActive())
                .build();
    }
}
