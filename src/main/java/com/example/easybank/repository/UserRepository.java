package com.example.easybank.repository;

import com.example.easybank.domain.entity.UserData;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends GenericRepository<UserData, UUID> {
    Optional<UserData> findByUsernameOrEmail(String username, String email);
    Optional<UserData> findByEmail(String email);
    Optional<UserData> findByUsername(String username);
    Optional<UserData> findByDui(String dui);
}
