package com.chatop.backend.dto;

import java.time.LocalDate;

import com.chatop.backend.entity.Rental;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class RentalResponseDto {

    private int id;
    private String name;

    private int surface;
    private int price;
    private String picture;
    private String description;
    private int owner_id;

    @JsonFormat(pattern = "yyyy-dd-MM")
    private LocalDate created_at;
    @JsonFormat(pattern = "yyyy-dd-MM")
    private LocalDate updated_at;

    public RentalResponseDto(Rental rental) {

        id = rental.getId();
        name = rental.getName();
        surface = rental.getSurface();
        price = rental.getPrice();
        picture = rental.getPicture();
        description = rental.getDescription();
        owner_id = rental.getOwner().getId();
        created_at = rental.getCreated_at();
        updated_at = rental.getUpdated_at();
    }
}
