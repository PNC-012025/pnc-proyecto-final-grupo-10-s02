package com.example.easybank.service;

import com.example.easybank.domain.dto.request.TransactionRequestDTO;
import com.example.easybank.domain.dto.response.TransactionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
    public void createTransaction(TransactionRequestDTO transactionRequestDTO) throws Exception;
    public Page<TransactionResponseDTO> getAllTransactions(
            String type,
            LocalDateTime from,
            LocalDateTime to,
            String accountId,
            String originAccountId,
            String destinationAccountId,
            Pageable pageable
    ) throws Exception;
}
