package com.example.easybank.repository;

import com.example.easybank.domain.entity.UserData;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends GenericRepository<UserData, UUID> {
    public Optional<UserData> findByUsernameOrEmail(String username, String email);
    public Optional<UserData> findByEmail(String email);
    public Optional<UserData> findByUsername(String username);
    public Optional<UserData> findByDui(String dui);
}
