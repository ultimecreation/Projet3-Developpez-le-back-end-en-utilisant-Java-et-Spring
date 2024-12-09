package com.chatop.backend.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.chatop.backend.dto.MessageResponseDto;
import com.chatop.backend.dto.RentalCreateRequestDto;
import com.chatop.backend.dto.RentalListResponseDto;
import com.chatop.backend.dto.RentalResponseDto;
import com.chatop.backend.dto.RentalUpdateRequestDto;
import com.chatop.backend.entity.Rental;
import com.chatop.backend.entity.User;
import com.chatop.backend.service.FileUploadService;
import com.chatop.backend.service.RentalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Rental")
@SecurityRequirement(name = "Bearer")
@RequestMapping("/api")
public class RentalController {
    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private RentalService rentalService;

    /**
     * @return returns the list of rentals
     */
    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "getAllRentalsSuccessRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @Parameter(in = ParameterIn.HEADER, description = "Bearer Token String Required", name = "Authorization")
    @GetMapping("/rentals")
    public RentalListResponseDto getAllRentals() {

        List<Rental> rentals = rentalService.getAllRentals();
        List<RentalResponseDto> rentalsListToReturn = rentals.stream()
                .map((Rental rental) -> new RentalResponseDto(rental))
                .collect(Collectors.toList());
        RentalListResponseDto rentalListResponseDto = new RentalListResponseDto(rentalsListToReturn);

        return rentalListResponseDto;
    }

    /**
     * @param id the rental id to retrieve
     * @return return the rental object
     */
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

    /**
     * @param file           the uploaded file
     * @param formData       the rest of incoming on behalf of file uploaded
     * @param authentication Springbbot Authentication class
     * @return return a response as object
     */
    @Operation(responses = {
            @ApiResponse(responseCode = "201", ref = "createRentalSuccessRequestApi"),
            @ApiResponse(responseCode = "400", ref = "createRentalBadRequestRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @Parameter(in = ParameterIn.HEADER, description = "Bearer Token String Required", name = "Authorization")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/rentals")
    public MessageResponseDto createRental(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "multipart/form-data", schema = @Schema(implementation = RentalCreateRequestDto.class))) @Valid RentalCreateRequestDto rentalCreateRequestDto,
            Authentication authentication) {

        // get authenticated user and computer filename
        User owner = (User) authentication.getPrincipal();
        String filePathToSaveInDb = this.fileUploadService.uploadFile(rentalCreateRequestDto.getPicture(),
                owner.getId());

        Rental rental = new Rental();
        rental.setName(rentalCreateRequestDto.getName());
        rental.setSurface(Integer.parseInt(rentalCreateRequestDto.getSurface()));
        rental.setPrice(Integer.parseInt(rentalCreateRequestDto.getPrice()));
        rental.setDescription(rentalCreateRequestDto.getDescription());
        rental.setOwner(owner);
        rental.setCreated_at(LocalDate.now());
        rental.setUpdated_at(LocalDate.now());
        if (filePathToSaveInDb != "") {
            rental.setPicture(filePathToSaveInDb);
        }
        rentalService.saveRental(rental);

        MessageResponseDto response = new MessageResponseDto("Rental created !");
        return response;
    }

    /**
     * @param file     incoming file data
     * @param id       the rental's id to update
     * @param formData the rest of incoming data on behalf of the file uploaded
     * @return return a response as json object
     */
    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "updateRentalSuccessRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @Parameter(in = ParameterIn.HEADER, description = "Bearer Token String Required", name = "Authorization")
    @PutMapping("/rentals/{id}")
    public ResponseEntity<Object> updateRental(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "multipart/form-data", schema = @Schema(implementation = RentalUpdateRequestDto.class)))

            @PathVariable int id,
            @Valid RentalUpdateRequestDto rentalUpdateRequestDto) {

        Rental rental = rentalService.getRentalById(id);
        this.updateRentalData(rental, rentalUpdateRequestDto);

        // update file if any submitted
        String filePathToSaveInDb = "";
        if (rentalUpdateRequestDto.getPicture() != null) {
            filePathToSaveInDb = this.fileUploadService.uploadFile(rentalUpdateRequestDto.getPicture(), id);
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

    /**
     * @param rental                 the rental object to loop on
     * @param rentalUpdateRequestDto the incoming data to check against the rental
     *                               object
     * @return a errors hash map if any errors are found
     */
    public Rental updateRentalData(Rental rental, RentalUpdateRequestDto rentalUpdateRequestDto) {
        if (rentalUpdateRequestDto.getName() != null) {
            rental.setName(rentalUpdateRequestDto.getName());
        }
        if (rentalUpdateRequestDto.getSurface() != null) {
            rental.setSurface(Integer.parseInt(rentalUpdateRequestDto.getSurface()));
        }
        if (rentalUpdateRequestDto.getPrice() != null) {
            rental.setPrice(Integer.parseInt(rentalUpdateRequestDto.getPrice()));
        }
        if (rentalUpdateRequestDto.getDescription() != null) {
            rental.setDescription(rentalUpdateRequestDto.getDescription());
        }
        // if (formData.get("created_at") != null) {
        // rental.setCreated_at(LocalDate.parse(formData.get("created_at")));
        // }
        // if (formData.get("updated_at") != null) {
        // rental.setUpdated_at(LocalDate.now());
        // }
        return rental;
    }

    /**
     * @param file     incoming file
     * @param formData incoming form data
     * @return returns a hash map of errors if any
     */
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

        return errors;
    }
}
