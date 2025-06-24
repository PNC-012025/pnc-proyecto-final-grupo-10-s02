package com.example.easybank.service;

import com.example.easybank.domain.dto.request.TransactionRequestDTO;
import com.example.easybank.domain.dto.response.AdminTransactionResponseDTO;
import com.example.easybank.domain.dto.response.TransactionResponseDTO;

import java.util.List;

public interface TransactionService {
    public void createTransaction(TransactionRequestDTO transactionRequestDTO) throws Exception;
    public List<TransactionResponseDTO> findMyOwnTransactions() throws Exception;
    public List<AdminTransactionResponseDTO> findAllTransactions() throws Exception;
}
