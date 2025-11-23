package com.example.easybank.repository;

import com.example.easybank.domain.entity.Bill;
import com.example.easybank.domain.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BillRepository extends JpaRepository<Bill, UUID> {
    List<Bill> getBillsById(UUID id);
    List<Bill> findBillsByStateAndUser(String state, UserData user);
}
