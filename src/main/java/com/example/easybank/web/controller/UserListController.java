package com.example.easybank.web.controller;


import com.example.easybank.application.dto.response.UserResponseDTO;
import com.example.easybank.application.service.UserListService;
import com.example.easybank.util.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}








