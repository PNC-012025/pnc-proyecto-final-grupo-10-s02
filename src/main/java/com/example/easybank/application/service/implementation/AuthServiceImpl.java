package com.example.easybank.application.service.implementation;

import com.example.easybank.application.dto.AccountCreateDTO;
import com.example.easybank.application.dto.request.LoginDTO;
import com.example.easybank.application.dto.request.RegisterDTO;
import com.example.easybank.application.dto.response.TokenResponse;
import com.example.easybank.application.dto.response.UserResponseDTO;
import com.example.easybank.application.mapper.UserMapper;
import com.example.easybank.application.service.AccountService;
import com.example.easybank.application.service.AuthService;
import com.example.easybank.domain.entity.Role;
import com.example.easybank.domain.entity.UserData;
import com.example.easybank.domain.exception.AlreadyExistsException;
import com.example.easybank.domain.exception.ModelNotFoundException;
import com.example.easybank.domain.exception.StorageException;
import com.example.easybank.infrastructure.repository.RoleRepository;
import com.example.easybank.infrastructure.repository.UserRepository;
import com.example.easybank.infrastructure.security.JwtTokenProvider;
import com.example.easybank.util.AccountNumberGenerator;
import com.example.easybank.util.PasswordUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountService accountServiceImpl;
    private final RoleRepository roleRepository;

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

        Optional<Role> role = roleRepository.findByName("ROLE_USER");
        Role userRole = role.orElseThrow(() -> new EntityNotFoundException("Role not found"));
        user.setRoles(Set.of(userRole));


        UserData userSaved = userRepository.save(user);

        AccountCreateDTO accountCreateDTO = AccountCreateDTO.builder()
                .number(AccountNumberGenerator.generateAccountNumber())
                .balance(new BigDecimal("50.00"))
                .type("STANDARD")
                .currency("USD")
                .userData(userSaved)
                .build();

        try{
            accountServiceImpl.save(accountCreateDTO);
        }
        catch (Exception e){
            throw new StorageException("Failed to register account");
        }

    }

    @Override
    public TokenResponse login(LoginDTO loginDTO) throws Exception {

        String rawPassword = "admin123";
        String hashed = "$2a$10$uU2LbWpxEbTfw06jgrUZGOGJDNZ5Y09PPN8WWpJhkfvP9uyx2guVa";

        boolean match = new BCryptPasswordEncoder().matches(rawPassword, hashed);
        System.out.println("¿Coincide? " + match); // debe imprimir true
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

        UserData user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ModelNotFoundException("User not found"));

        user.setActive(!user.getCards().isEmpty());
        UserResponseDTO whoamiResponse = UserMapper.toDTO(user);

        whoamiResponse.setRoles(user.getRoles().stream().map(Role::getName).toList());

        return whoamiResponse;
    }
}
