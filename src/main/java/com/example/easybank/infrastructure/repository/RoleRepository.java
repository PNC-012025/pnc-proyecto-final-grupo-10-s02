package com.example.easybank.infrastructure.repository;

import com.example.easybank.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    public Optional<Role> findByName(String name);
}
