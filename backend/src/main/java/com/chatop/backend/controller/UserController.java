package com.chatop.backend.controller;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "User")
@SecurityRequirement(name = "Bearer")
@RequestMapping("/api")
public class UserController {

    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "userSuccessRequestApi"),
            @ApiResponse(responseCode = "400", ref = "userBadRequestRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<?> messages(@PathVariable int id) {
        // TODO: process POST request

        var response = new HashMap<String, String>();
        response.put("id", "1");
        response.put("name", "Owner Name");
        response.put("email", "1");
        response.put("created_at", "2022/02/02");
        response.put("updated_at", "2022/08/02");
        return ResponseEntity.ok(response);
    }
}
