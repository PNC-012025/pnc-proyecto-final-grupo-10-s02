package com.example.easybank.infrastructure.repository;

import com.example.easybank.domain.entity.Account;
import com.example.easybank.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    public Optional<Transaction> findTransactionsByOriginAccount(Account originAccount);
}
