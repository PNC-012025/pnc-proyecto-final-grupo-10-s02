package com.example.easybank.application.service.implementation;

import com.example.easybank.application.dto.request.LoginDTO;
import com.example.easybank.application.dto.request.RegisterDTO;
import com.example.easybank.application.dto.response.TokenResponse;
import com.example.easybank.application.dto.response.UserResponseDTO;
import com.example.easybank.application.mapper.UserMapper;
import com.example.easybank.application.service.AuthService;
import com.example.easybank.domain.entity.UserData;
import com.example.easybank.domain.exception.AlreadyExistsException;
import com.example.easybank.infrastructure.repository.UserRepository;
import com.example.easybank.infrastructure.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void register(RegisterDTO registerDTO) throws Exception {
        UserData userToVerify = userRepository.findByUsername(registerDTO.getUsername());
        if (userToVerify != null) throw new AlreadyExistsException("Username already exists");
        userToVerify = userRepository.findByEmail(registerDTO.getEmail());
        if (userToVerify != null) throw new AlreadyExistsException("Email already exists");
        userToVerify = userRepository.findByDui(registerDTO.getDui());
        if (userToVerify != null) throw new AlreadyExistsException("Dui already exists");

        UserData user = UserMapper.toEntity(registerDTO);
        userRepository.save(user);
    }

    @Override
    public TokenResponse login(LoginDTO loginDTO) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        return TokenResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public UserResponseDTO whoami() throws Exception {
        return null;
    }
}
