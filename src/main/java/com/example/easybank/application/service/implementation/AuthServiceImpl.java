package com.example.easybank.application.service.implementation;

import com.example.easybank.application.dto.AccountCreateDTO;
import com.example.easybank.application.dto.request.LoginDTO;
import com.example.easybank.application.dto.request.RegisterDTO;
import com.example.easybank.application.dto.response.TokenResponse;
import com.example.easybank.application.dto.response.UserResponseDTO;
import com.example.easybank.application.mapper.UserMapper;
import com.example.easybank.application.service.AuthService;
import com.example.easybank.domain.entity.UserData;
import com.example.easybank.domain.exception.AlreadyExistsException;
import com.example.easybank.domain.exception.ModelNotFoundException;
import com.example.easybank.infrastructure.repository.UserRepository;
import com.example.easybank.infrastructure.security.JwtTokenProvider;
import com.example.easybank.util.AccountNumberGenerator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountServiceImpl accountServiceImpl;

    @Override
    @Transactional
    public void register(RegisterDTO registerDTO) throws Exception {

        userRepository.findByUsername(registerDTO.getUsername())
                .ifPresent(user -> {
                    throw new AlreadyExistsException("User already exists");
                });
        userRepository.findByEmail(registerDTO.getEmail())
                .ifPresent(user -> {
                    throw new AlreadyExistsException("User already exists");
                });
        userRepository.findByDui(registerDTO.getDui())
                .ifPresent(user -> {
                    throw new AlreadyExistsException("User already exists");
                });

        UserData user = UserMapper.toEntity(registerDTO);
        UserData userSaved = userRepository.save(user);

        AccountCreateDTO accountCreateDTO = AccountCreateDTO.builder()
                .number(AccountNumberGenerator.generateAccountNumber())
                .balance(new BigDecimal("50.00"))
                .type("STANDARD")
                .currency("USD")
                .userData(userSaved)
                .build();

         accountServiceImpl.save(accountCreateDTO);
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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByUsername(username)
                .map(UserMapper::toDTO)
                .orElseThrow(() -> new ModelNotFoundException("User not found"));
    }
}
