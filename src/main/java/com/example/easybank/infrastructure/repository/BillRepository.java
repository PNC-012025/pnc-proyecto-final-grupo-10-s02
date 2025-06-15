package com.example.easybank.infrastructure.repository;

import com.example.easybank.domain.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BillRepository extends JpaRepository<Bill, UUID> {
    public List<Bill> getBillsById(UUID id);
}
