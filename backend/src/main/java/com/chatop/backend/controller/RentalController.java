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
import com.chatop.backend.service.FileUploadService;
import com.chatop.backend.service.RentalService;
import com.chatop.backend.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
    RentalService rentalService;

    @Autowired
    UserService userService;

    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "getAllRentalsSuccessRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @Parameter(in = ParameterIn.HEADER, description = "Bearer Token String Required", name = "Authorization")
    @GetMapping("/rentals")
    public ResponseEntity<HashMap<String, List<RentalResponseDto>>> getAllRentals() {

        List<Rental> rentals = rentalService.getAllRentals();
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
    public ResponseEntity<RentalResponseDto> getRentalById(@PathVariable int id) {

        Rental rental = rentalService.getRentalById(id);
        RentalResponseDto rentalResponseDto = new RentalResponseDto(rental);

        return ResponseEntity.ok(rentalResponseDto);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "createRentalSuccessRequestApi"),
            @ApiResponse(responseCode = "400", ref = "createRentalBadRequestRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @Parameter(in = ParameterIn.HEADER, description = "Bearer Token String Required", name = "Authorization")
    @PostMapping("/rentals")
    public ResponseEntity<Object> createRental(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The request has to be send with enctype='multipart/form-data'. All the fields are optional.", required = false, content = @Content(mediaType = "application/json", schema = @Schema(implementation = Rental.class), examples = @ExampleObject(value = "{ \"picture\": \"File\" ,\"name\": \"string\", \"surface\": \"string\", \"price\": \"string\", \"description\": \"string\", \"created_at\": \"string\", \"update_at\": \"string\"}"))) @RequestParam(value = "picture", required = false) MultipartFile file,
            @RequestParam(required = false) HashMap<String, String> formData) {

        HashMap<String, String> errorsMap = this.getValidationErrors(file, formData);
        if (!errorsMap.isEmpty()) {
            return ResponseEntity.badRequest().body(errorsMap);
        }

        User owner = userService.getUserById(Integer.parseInt(formData.get("owner_id")));
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
            rentalService.saveRental(rental);
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
    public ResponseEntity<Object> updateRental(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The request has to be send with enctype='multipart/form-data'. All the fields are optional.", required = false, content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"picture\": \"File\" ,\"name\": \"string\", \"surface\": \"string\", \"price\": \"string\", \"description\": \"string\", \"created_at\": \"string\", \"update_at\": \"string\"}"))) @RequestParam(name = "picture", required = false) MultipartFile file,
            @PathVariable int id,
            @RequestParam(required = false) HashMap<String, String> formData) {

        Rental rental = rentalService.getRentalById(id);
        this.updateRentalData(rental, formData);

        String filePathToSaveInDb = "";
        if (file != null) {
            filePathToSaveInDb = this.fileUploadService.uploadFile(file, id);
        }
        if (filePathToSaveInDb != "") {
            rental.setPicture(filePathToSaveInDb);
        }

        try {
            rentalService.saveRental(rental);
            var response = new HashMap<String, String>();
            response.put("message", "Rental updated !");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("Error while saving rental" + e);
        }

        return ResponseEntity.badRequest().body("An unexpected error occured");

    }

    public Rental updateRentalData(Rental rental, HashMap<String, String> formData) {
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
        return rental;
    }

    public HashMap<String, String> getValidationErrors(MultipartFile file, HashMap<String, String> formData) {
        var errors = new HashMap<String, String>();
        if (formData.get("name") == null) {
            errors.put("name", "name is required");
        }
        if (formData.get("surface") == null) {
            errors.put("surface", "surface is required");
        }
        if (formData.get("price") == null) {
            errors.put("price", "price is required");
        }
        if (file == null) {
            errors.put("file", "picture is required");
        }
        if (formData.get("description") == null) {
            errors.put("description", "description is required");
        }
        if (formData.get("owner_id") == null) {
            errors.put("owner_id", "owner_id is required");
        }
        if (formData.get("created_at") == null) {
            errors.put("created_at", "created_at is required");
        }
        if (formData.get("updated_at") == null) {
            errors.put("updated_at", "updated_at is required");
        }

        return errors;
    }
}
