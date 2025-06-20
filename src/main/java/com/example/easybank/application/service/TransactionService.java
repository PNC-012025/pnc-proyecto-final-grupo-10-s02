package com.example.easybank.application.service;

import com.example.easybank.application.dto.request.TransactionRequestDTO;
import com.example.easybank.application.dto.response.TransactionResponseDTO;
import com.example.easybank.domain.entity.Transaction;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    public void createTransaction(TransactionRequestDTO transactionRequestDTO) throws Exception;
    public List<TransactionResponseDTO> findMyOwnTransactions() throws Exception;
}
