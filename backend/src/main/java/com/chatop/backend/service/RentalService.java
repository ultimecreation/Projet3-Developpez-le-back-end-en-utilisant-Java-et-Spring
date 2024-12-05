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

    /**
     * @return the list of rentals
     */
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    /**
     * @param id represents the rental id
     * @return returns the rental by it's id
     */
    public Rental getRentalById(int id) {
        return rentalRepository.findById(id).orElseThrow();
    }

    /**
     * @param rental rental object to save
     * @return returns the repositoy response
     */
    public Rental saveRental(Rental rental) {
        return rentalRepository.save(rental);
    }
}
