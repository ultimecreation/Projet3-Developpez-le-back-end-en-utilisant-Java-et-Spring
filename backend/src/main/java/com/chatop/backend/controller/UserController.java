package com.chatop.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.backend.dto.UserResponseDto;
import com.chatop.backend.entity.User;
import com.chatop.backend.service.UserService;

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
    UserService userService;

    /**
     * @param id to user id linked with the message
     * @return returns a json object with the user or an error
     */
    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "userSuccessRequestApi"),
            @ApiResponse(responseCode = "400", ref = "userBadRequestRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @Parameter(in = ParameterIn.HEADER, description = "Bearer Token String Required", name = "Authorization")
    @GetMapping("/user/{id}")
    public ResponseEntity<Object> messages(@PathVariable int id) {
        User user = this.userService.getUserById(id);
        if (user != null) {
            UserResponseDto userToReturn = new UserResponseDto(user);
            return ResponseEntity.ok(userToReturn);
        }
        return ResponseEntity.badRequest().body("An unexpected error occured");
    }
}
