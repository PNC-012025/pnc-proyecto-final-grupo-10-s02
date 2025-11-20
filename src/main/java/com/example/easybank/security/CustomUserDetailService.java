package com.example.easybank.security;

import com.example.easybank.domain.entity.UserData;
import com.example.easybank.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        UserData user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(usernameOrEmail));

        Set<GrantedAuthority> grantedAuthorities = user.getRoles()
                .stream()
                .map(role -> {
                    String name = role.getName();
                    
                    if (!name.startsWith("ROLE_")) {
                        name = "ROLE_" + name;
                    }

                    return new SimpleGrantedAuthority(name);
                })
                .collect(Collectors.toSet());

        return new User(user.getUsername(), user.getHashedPassword(), grantedAuthorities);
    }

}
