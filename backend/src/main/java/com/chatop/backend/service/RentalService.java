package com.chatop.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatop.backend.entity.Rental;
import com.chatop.backend.repository.RentalRepository;

@Service
public class RentalService {

    @Autowired
    RentalRepository rentalRepository;

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public Rental getRentalById(int id) {
        return rentalRepository.findById(id).orElseThrow();
    }

    public Rental saveRental(Rental rental) {
        return rentalRepository.save(rental);
    }
}
