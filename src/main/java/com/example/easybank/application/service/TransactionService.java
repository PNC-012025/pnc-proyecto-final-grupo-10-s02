package com.example.easybank.application.service;

import com.example.easybank.application.dto.request.TransactionRequestDTO;
import com.example.easybank.application.dto.response.TransactionResponseDTO;
import com.example.easybank.domain.entity.Transaction;

import java.util.List;

public interface TransactionService {
    public void createTransaction(TransactionRequestDTO transactionRequestDTO) throws Exception;
    public List<TransactionResponseDTO> findMyOwnTransactions() throws Exception;
}
