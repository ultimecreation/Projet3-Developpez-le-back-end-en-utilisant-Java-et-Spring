package com.chatop.backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalListResponseDto {

    List<RentalResponseDto> rentals;

    // public List<RentalResponseDto> getRentals() {
    // return this.rentals;
    // }

    // public List<RentalResponseDto> setRentals(List<RentalResponseDto>
    // incomingRentals) {
    // return this.rentals = incomingRentals;
    // }
}
