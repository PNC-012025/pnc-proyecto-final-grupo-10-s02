package com.example.easybank.domain.dto.request;

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
    @NotBlank(message = "DUI is necessary")
    private String dui;

    @NotBlank(message = "Username is necessary")
    private String username;

    @NotBlank(message = "First Name is necessary")
    private String firstName;

    @NotBlank(message = "Last Name is necessary")
    private String lastName;


    @NotBlank(message = "Email is necessary")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is necessary")
    private String password;
}
