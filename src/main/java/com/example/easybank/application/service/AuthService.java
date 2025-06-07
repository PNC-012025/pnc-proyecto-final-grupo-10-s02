package com.example.easybank.application.service;

import com.example.easybank.application.dto.request.LoginDTO;
import com.example.easybank.application.dto.request.RegisterDTO;
import com.example.easybank.application.dto.response.TokenResponse;
import com.example.easybank.application.dto.response.UserResponseDTO;

public interface AuthService {
    public void register(RegisterDTO registerDTO) throws Exception;
    public TokenResponse login(LoginDTO loginDTO) throws Exception;
    public UserResponseDTO whoami() throws Exception;
}
