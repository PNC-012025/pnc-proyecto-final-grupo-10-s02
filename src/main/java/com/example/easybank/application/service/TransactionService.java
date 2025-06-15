package com.example.easybank.application.service;

import com.example.easybank.application.dto.request.TransactionRequestDTO;
import com.example.easybank.domain.entity.Transaction;

public interface TransactionService {
    public void createTransaction(TransactionRequestDTO transactionRequestDTO) throws Exception;
}
