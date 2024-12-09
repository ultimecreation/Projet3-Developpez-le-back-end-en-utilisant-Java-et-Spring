package com.chatop.backend.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalCreateRequestDto {

    @NotNull(message = "name is required")
    private String name;

    @NotNull(message = "surface is required")
    private String surface;

    @NotNull(message = "price is required")
    private String price;

    @NotNull(message = "picture is required")
    private MultipartFile picture;

    @NotNull(message = "description is required")
    private String description;

}
