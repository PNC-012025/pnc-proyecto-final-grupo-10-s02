package com.example.easybank.web.controller;


import com.example.easybank.application.dto.request.ChangeRoleRequestDTO;
import com.example.easybank.application.dto.response.UserResponseDTO;
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








}








