package com.example.easybank.repository;

import com.example.easybank.domain.entity.Account;
import com.example.easybank.domain.entity.Transaction;
import com.example.easybank.domain.entity.UserData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Optional<Transaction> findTransactionsByOriginAccount(Account originAccount);

    List<Transaction> findByOriginAccount_User_IdOrDestinationAccount_User_IdOrderByDateTimeDesc(
            UUID originUserId,
            UUID destinationUserId
    );


}
