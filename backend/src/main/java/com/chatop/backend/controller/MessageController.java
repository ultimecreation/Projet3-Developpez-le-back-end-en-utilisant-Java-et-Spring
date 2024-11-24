package com.chatop.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.chatop.backend.dto.MessageRequestDto;
import com.chatop.backend.entity.Message;
import com.chatop.backend.entity.Rental;
import com.chatop.backend.entity.User;
import com.chatop.backend.repository.MessageRepository;
import com.chatop.backend.repository.RentalRepository;
import com.chatop.backend.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@Tag(name = "Message")
@SecurityRequirement(name = "Bearer")
@RequestMapping("/api")
public class MessageController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    MessageRepository messageRepository;

    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "messageSuccessRequestApi"),
            @ApiResponse(responseCode = "400", ref = "messageBadRequestRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @Parameter(in = ParameterIn.HEADER, description = "Bearer Token String Required", name = "Authorization")
    @PostMapping("/messages")
    public ResponseEntity<?> messages(@Valid @RequestBody MessageRequestDto body, BindingResult result) {
        if (result.hasErrors()) {
            var errorsList = result.getAllErrors();
            var errorsMap = new HashMap<String, String>();
            for (int i = 0; i < errorsList.size(); i++) {
                var error = (FieldError) errorsList.get(i);
                errorsMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorsMap);
        }

        try {
            User user = userRepository.findById(Integer.parseInt(body.getUser_id())).orElseThrow();
            Rental rental = rentalRepository.findById(Integer.parseInt(body.getRental_id())).orElseThrow();
            Message message = new Message();
            message.setMessage(body.getMessage());
            message.setUser(user);
            message.setRental(rental);
            messageRepository.save(message);

            var response = new HashMap<String, String>();
            response.put("message", "Message send with success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Error while saving rental" + e);
            return ResponseEntity.badRequest().body("An unexpected error occured while saving the message");
        }
    }
}
