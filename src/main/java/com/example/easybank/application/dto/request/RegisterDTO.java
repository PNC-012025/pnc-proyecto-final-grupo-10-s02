package com.example.easybank.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDTO {
    @NotEmpty
    @NotNull
    @NotBlank
    private String dui;

    @NotEmpty
    @NotNull
    @NotBlank
    private String username;

    @NotEmpty
    @NotNull
    @NotBlank
    private String firstName;

    @NotEmpty
    @NotNull
    @NotBlank
    private String lastName;

    @NotEmpty
    @NotNull
    @NotBlank
    @Email(message = "")
    private String email;

    @NotEmpty
    @NotNull
    @NotBlank
    private String password;
}
