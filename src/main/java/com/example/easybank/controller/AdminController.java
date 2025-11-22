package com.example.easybank.controller;


import com.example.easybank.domain.dto.request.ChangeRoleRequestDTO;
import com.example.easybank.domain.dto.request.DepositRequestDTO;
import com.example.easybank.domain.dto.response.*;
import com.example.easybank.service.AdminService;
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
public class AdminController {
    private final AdminService adminService;


    @GetMapping(USER_LIST)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponse> getAllUsers() throws Exception {
        List<UserResponseDTO> users = adminService.findAllUsers();

        return GenericResponse.builder()
                .data(users)
                .message("Users found")
                .status(HttpStatus.OK)
                .build().buildResponse();

    }

    @GetMapping(USER_LIST + "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponse> getUserById(@PathVariable("id")  UUID id) throws Exception {
        UserResponseDTO user = adminService.getUserById(id);

        return GenericResponse.builder()
                .data(user)
                .message("User found")
                .status(HttpStatus.OK)
                .build().buildResponse();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(USER_LIST + DELETE + "/{id}")
    public ResponseEntity<GenericResponse> deleteUser(@PathVariable("id") UUID id) throws Exception {
        adminService.delete(id);
        return GenericResponse.builder()
                .status(HttpStatus.ACCEPTED)
                .message("Successfully deleted user")
                .build().buildResponse();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(USER_LIST + CHANGE_ROLE + "/{id}")
    public ResponseEntity<GenericResponse> changeUserRoles(@PathVariable ("id") UUID id, @RequestBody ChangeRoleRequestDTO request) {

        adminService.changeRoles(id, request.getRoles());

        return GenericResponse.builder()
                .status(HttpStatus.OK)
                .message("Roles updated successfully")
                .build().buildResponse();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(USER_LIST + "/{id}/accounts")
    public ResponseEntity<GenericResponse> getUserAccounts(@PathVariable ("id") UUID id) {
        List<AccountResponseAdminDTO> accounts = adminService.getUserAccounts(id);
        return GenericResponse.builder()
                .data(accounts)
                .message("User's accounts found")
                .status(HttpStatus.OK)
                .build().buildResponse();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(USER_LIST + "/{id}/bills")
    public ResponseEntity<GenericResponse> getUserBills(@PathVariable ("id") UUID id) throws Exception {
        List<BillResponseDTO> bills = adminService.getUserBills(id);
        return GenericResponse.builder()
                .data(bills)
                .message("User's bills found")
                .status(HttpStatus.OK)
                .build().buildResponse();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(USER_LIST + "/{id}/deposit")
    public ResponseEntity<GenericResponse> depositToUserAccount(
            @PathVariable UUID id,
            @RequestBody DepositRequestDTO request) {

        adminService.depositToUserAccount(id, request.getAccountId(), request.getAmount(), request.getDescription());

        return GenericResponse.builder()
                .status(HttpStatus.OK)
                .message("Deposit completed successfully")
                .build().buildResponse();
    }


    @GetMapping(TRANSACTION)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getTransactions(
            @RequestParam(required = false) UUID id,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int page
    ) {
        return GenericResponse.builder()
                .data(adminService.getUserTransactions(id, limit, page))
                .status(HttpStatus.OK)
                .message("Transactions retrieved")
                .build()
                .buildResponse();
    }


}








