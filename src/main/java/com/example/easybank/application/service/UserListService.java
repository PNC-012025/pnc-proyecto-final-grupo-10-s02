package com.example.easybank.application.service;


import com.example.easybank.application.dto.response.AccountResponseDTO;
import com.example.easybank.application.dto.response.BillResponseDTO;
import com.example.easybank.application.dto.response.TransactionResponseDTO;
import com.example.easybank.application.dto.response.UserResponseDTO;

import java.util.List;
import java.util.UUID;

public interface UserListService {
    List<UserResponseDTO> findAllUsers() throws Exception;
    void delete(UUID id) throws Exception;
    void changeRoles(UUID id, List<String> roles);
    UserResponseDTO getUserById(UUID id);
    List<AccountResponseDTO> getUserAccounts(UUID id);
    List<BillResponseDTO> getUserBills(UUID id);
    List<TransactionResponseDTO> getUserTransactions(UUID userId, int limit, int page);

}
