package com.chatop.backend.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.chatop.backend.dto.RentalResponseDto;
import com.chatop.backend.entity.Rental;
import com.chatop.backend.entity.User;
import com.chatop.backend.repository.RentalRepository;
import com.chatop.backend.repository.UserRepository;
import com.chatop.backend.service.FileUploadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Rental")
@SecurityRequirement(name = "Bearer")
@RequestMapping("/api")
public class RentalController {
    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RentalRepository rentalRepository;

    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "getAllRentalsSuccessRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @Parameter(in = ParameterIn.HEADER, description = "Bearer Token String Required", name = "Authorization")
    @GetMapping("/rentals")
    public ResponseEntity<HashMap<String, List<RentalResponseDto>>> getAllRentals() {

        List<Rental> rentals = rentalRepository.findAll();
        var response = new HashMap<String, List<RentalResponseDto>>();

        response.put("rentals", rentals
                .stream()
                .map((rental) -> new RentalResponseDto(rental))
                .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "getSingleRentalSuccessRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @Parameter(in = ParameterIn.HEADER, description = "Bearer Token String Required", name = "Authorization")
    @GetMapping("/rentals/{id}")
    public ResponseEntity<?> getRentalById(@PathVariable int id) {

        Rental rental = rentalRepository.findById(id).orElseThrow();
        RentalResponseDto rentalResponseDto = new RentalResponseDto(rental);

        return ResponseEntity.ok(rentalResponseDto);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "createRentalSuccessRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @Parameter(in = ParameterIn.HEADER, description = "Bearer Token String Required", name = "Authorization")
    @PostMapping("/rentals")
    public ResponseEntity<?> createRental(@RequestParam("picture") MultipartFile file,
            @RequestParam HashMap<String, String> formData) {

        User owner = userRepository.findById(Integer.parseInt(formData.get("owner_id"))).orElseThrow();

        String filePathToSaveInDb = this.fileUploadService.uploadFile(file, owner.getId());

        Rental rental = new Rental();
        rental.setName(formData.get("name"));
        rental.setSurface(Integer.parseInt(formData.get("surface")));
        rental.setPrice(Integer.parseInt(formData.get("price")));
        rental.setDescription(formData.get("description"));
        rental.setOwner(owner);
        rental.setCreated_at(LocalDate.parse(formData.get("created_at")));
        rental.setUpdated_at(LocalDate.parse(formData.get("updated_at")));
        if (filePathToSaveInDb != "") {
            rental.setPicture(filePathToSaveInDb);
        }
        try {
            rentalRepository.save(rental);
            var response = new HashMap<String, String>();
            response.put("message", "Rental created !");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("Error while saving rental" + e);
        }

        return ResponseEntity.badRequest().body("An unexpected error occured");
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "updateRentalSuccessRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @Parameter(in = ParameterIn.HEADER, description = "Bearer Token String Required", name = "Authorization")
    @PutMapping("/rentals/{id}")
    public ResponseEntity<?> updateRental(
            @RequestParam(name = "picture", required = false) MultipartFile file,
            @PathVariable int id,
            @RequestParam HashMap<String, String> formData) {

        Rental rental = rentalRepository.findById(id).orElseThrow();

        if (formData.get("name") != null) {
            rental.setName(formData.get("name"));
        }
        if (formData.get("surface") != null) {
            rental.setSurface(Integer.parseInt(formData.get("surface")));
        }
        if (formData.get("price") != null) {
            rental.setPrice(Integer.parseInt(formData.get("price")));
        }
        if (formData.get("description") != null) {
            rental.setDescription(formData.get("description"));
        }
        if (formData.get("created_at") != null) {
            rental.setCreated_at(LocalDate.parse(formData.get("created_at")));
        }
        if (formData.get("updated_at") != null) {
            rental.setUpdated_at(LocalDate.parse(formData.get("updated_at")));
        }

        String filePathToSaveInDb = "";
        if (file != null) {
            filePathToSaveInDb = this.fileUploadService.uploadFile(file, id);
        }
        if (filePathToSaveInDb != "") {
            rental.setPicture(filePathToSaveInDb);
        }

        try {
            rentalRepository.save(rental);
            var response = new HashMap<String, String>();
            response.put("message", "Rental updated !");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("Error while saving rental" + e);
        }

        return ResponseEntity.badRequest().body("An unexpected error occured");

    }
}
