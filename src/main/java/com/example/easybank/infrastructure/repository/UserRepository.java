package com.example.easybank.infrastructure.repository;

import com.example.easybank.domain.entity.UserData;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends GenericRepository<UserData, UUID> {
    public Optional<UserData> findByUsernameOrEmail(String username, String email);
    public UserData findByEmail(String email);
    public UserData findByUsername(String username);
    public UserData findByDui(String dui);
}
