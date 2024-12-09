package com.chatop.backend.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalUpdateRequestDto {

    private String name;

    private String surface;

    private String price;

    private MultipartFile picture;

    private String description;
}
