package com.chatop.backend.controller;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Rental")
@SecurityRequirement(name = "Bearer")
@RequestMapping("/api")
public class RentalController {

    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "getAllRentalsSuccessRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @GetMapping("/rentals")
    public ResponseEntity<?> getAllRentals() {
        // TODO: process POST request

        var response = new HashMap<String, String>();
        response.put("rentals", "rentals list");

        return ResponseEntity.ok(response);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "getSingleRentalSuccessRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @GetMapping("/rentals/{id}")
    public ResponseEntity<?> getRentalById(@PathVariable int id) {
        // TODO: process POST request

        var response = new HashMap<String, String>();
        response.put("rental", "single rental");

        return ResponseEntity.ok(response);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "createRentalSuccessRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @PostMapping("/rentals")
    public ResponseEntity<?> createRental(@PathVariable int id) {
        // TODO: process POST request

        var response = new HashMap<String, String>();
        response.put("message", "Rental created !");

        return ResponseEntity.ok(response);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "updateRentalSuccessRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @PutMapping("/rentals/{id}")
    public ResponseEntity<?> updateRental(@PathVariable int id) {
        // TODO: process POST request

        var response = new HashMap<String, String>();
        response.put("message", "Rental updated !");

        return ResponseEntity.ok(response);
    }
}
