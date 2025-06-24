package com.example.easybank.web.controller;


import com.example.easybank.application.dto.request.ChangeRoleRequestDTO;
import com.example.easybank.application.dto.request.DepositRequestDTO;
import com.example.easybank.application.dto.response.*;
import com.example.easybank.application.service.UserListService;
import com.example.easybank.util.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.example.easybank.util.Constant.*;

@RequestMapping(API+ADMIN)
@RequiredArgsConstructor
@RestController
public class UserListController {
    private final UserListService userListService;


    @GetMapping(USER_LIST)
    public ResponseEntity<GenericResponse> getAllUsers() throws Exception {
        List<UserResponseDTO> users = userListService.findAllUsers();

        return GenericResponse.builder()
                .data(users)
                .message("Users found")
                .status(HttpStatus.OK)
                .build().buildResponse();

    }

    @GetMapping(USER_LIST + "/{id}")
    public ResponseEntity<GenericResponse> getUserById(@PathVariable("id")  UUID id) throws Exception {
        UserResponseDTO user = userListService.getUserById(id);

        return GenericResponse.builder()
                .data(user)
                .message("User found")
                .status(HttpStatus.OK)
                .build().buildResponse();
    }


    @DeleteMapping(USER_LIST + DELETE + "/{id}")
    public ResponseEntity<GenericResponse> deleteUser(@PathVariable("id") UUID id) throws Exception {
        userListService.delete(id);
        return GenericResponse.builder()
                .status(HttpStatus.ACCEPTED)
                .message("Successfully deleted user")
                .build().buildResponse();
    }


    @PutMapping(USER_LIST + CHANGE_ROLE + "/{id}")
    public ResponseEntity<GenericResponse> changeUserRoles(@PathVariable ("id") UUID id, @RequestBody ChangeRoleRequestDTO request) {

        userListService.changeRoles(id, request.getRoles());

        return GenericResponse.builder()
                .status(HttpStatus.OK)
                .message("Roles updated successfully")
                .build().buildResponse();
    }

    @GetMapping(USER_LIST + "/{id}/accounts")
    public ResponseEntity<GenericResponse> getUserAccounts(@PathVariable ("id") UUID id) {
        List<AccountResponseAdminDTO> accounts = userListService.getUserAccounts(id);
        return GenericResponse.builder()
                .data(accounts)
                .message("User's accounts found")
                .status(HttpStatus.OK)
                .build().buildResponse();
    }

    @GetMapping(USER_LIST + "/{id}/bills")
    public ResponseEntity<GenericResponse> getUserBills(@PathVariable ("id") UUID id) throws Exception {
        List<BillResponseDTO> bills = userListService.getUserBills(id);
        return GenericResponse.builder()
                .data(bills)
                .message("User's bills found")
                .status(HttpStatus.OK)
                .build().buildResponse();
    }

    @GetMapping(USER_LIST + "/{id}/transactions")
    public ResponseEntity<GenericResponse> getUserTransactions(
            @PathVariable ("id") UUID id,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int page) {

        List<TransactionResponseDTO> transactions = userListService.getUserTransactions(id, limit, page);

        return GenericResponse.builder()
                .data(transactions)
                .message("User's transactions found")
                .status(HttpStatus.OK)
                .build().buildResponse();
    }


    @PostMapping(USER_LIST + "/{id}/deposit")
    public ResponseEntity<GenericResponse> depositToUserAccount(
            @PathVariable UUID id,
            @RequestBody DepositRequestDTO request) {

        userListService.depositToUserAccount(id, request.getAccountId(), request.getAmount(), request.getDescription());

        return GenericResponse.builder()
                .status(HttpStatus.OK)
                .message("Deposit completed successfully")
                .build().buildResponse();
    }










}








