package com.chatop.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.backend.dto.UserResponseDto;
import com.chatop.backend.entity.User;
import com.chatop.backend.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "User")
@SecurityRequirement(name = "Bearer")
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "userSuccessRequestApi"),
            @ApiResponse(responseCode = "400", ref = "userBadRequestRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @Parameter(in = ParameterIn.HEADER, description = "Bearer Token String Required", name = "Authorization")
    @GetMapping("/user/{id}")
    public ResponseEntity<?> messages(@PathVariable int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user != null) {
            UserResponseDto userToReturn = new UserResponseDto(user);
            return ResponseEntity.ok(userToReturn);
        }
        return ResponseEntity.badRequest().body("An unexpected error occured");
    }
}
