package com.chatop.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatop.backend.entity.Rental;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {

}
