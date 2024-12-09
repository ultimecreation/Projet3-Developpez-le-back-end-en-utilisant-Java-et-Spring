package com.chatop.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.chatop.backend.dto.MessageRequestDto;
import com.chatop.backend.dto.MessageResponseDto;
import com.chatop.backend.entity.Message;
import com.chatop.backend.entity.Rental;
import com.chatop.backend.entity.User;
import com.chatop.backend.service.MessageService;
import com.chatop.backend.service.RentalService;
import com.chatop.backend.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@Tag(name = "Message")
@SecurityRequirement(name = "Bearer")
@RequestMapping("/api")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private UserService userService;

    /**
     * @param body   the request body
     * @param result the errors found if any
     * @return
     */
    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "messageSuccessRequestApi"),
            @ApiResponse(responseCode = "400", ref = "messageBadRequestRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @Parameter(in = ParameterIn.HEADER, description = "Bearer Token String Required", name = "Authorization")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/messages")
    public MessageResponseDto messages(@Valid @RequestBody MessageRequestDto body) {

        User user = userService.getUserById(Integer.parseInt(body.getUser_id()));
        Rental rental = rentalService.getRentalById(Integer.parseInt(body.getRental_id()));

        Message message = new Message();
        message.setMessage(body.getMessage());
        message.setUser(user);
        message.setRental(rental);

        messageService.saveMessage(message);
        MessageResponseDto messageResponseDto = new MessageResponseDto("Message send with success");
        return messageResponseDto;

    }
}
