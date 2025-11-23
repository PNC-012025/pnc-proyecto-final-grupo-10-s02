package com.example.easybank.repository;

import com.example.easybank.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByOriginAccount_User_IdOrDestinationAccount_User_IdOrderByDateTimeDesc(
            UUID originUserId,
            UUID destinationUserId
    );


}
