package com.example.easybank.service;


import com.example.easybank.domain.dto.response.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface AdminService {
    List<UserResponseDTO> findAllUsers() throws Exception;
    void delete(UUID id) throws Exception;
    void changeRoles(UUID id, List<String> roles);
    UserResponseDTO getUserById(UUID id);
    List<AccountResponseAdminDTO> getUserAccounts(UUID id);
    List<BillResponseDTO> getUserBills(UUID id);
    List<AdminTransactionResponseDTO> getUserTransactions(UUID userId, int limit, int page);
    void depositToUserAccount(UUID userId, UUID accountId, BigDecimal amount, String description);
    public List<AdminTransactionResponseDTO> findAll() throws Exception;

}
