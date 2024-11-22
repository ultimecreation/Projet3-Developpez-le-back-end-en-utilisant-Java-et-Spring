package com.chatop.backend.dto;

import com.chatop.backend.validation.UniqueEmailConstraint;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegisterDto {

    @NotEmpty(message = "name is required")
    private String name;

    @NotEmpty(message = "email is required")
    @UniqueEmailConstraint
    private String email;

    @NotEmpty(message = "password is required")
    private String password;

}
