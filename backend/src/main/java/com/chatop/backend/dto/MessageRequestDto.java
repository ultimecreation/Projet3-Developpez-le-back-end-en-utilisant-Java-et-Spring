package com.chatop.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MessageRequestDto {

    @NotEmpty(message = "user id is required")
    private String user_id;

    @NotEmpty(message = "rental id is required")
    private String rental_id;

    @NotEmpty(message = "message is required")
    private String message;
}
