package com.chatop.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@Tag(name = "Message")
@SecurityRequirement(name = "Bearer")
@RequestMapping("/api")
public class MessageController {

    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "messageSuccessRequestApi"),
            @ApiResponse(responseCode = "400", ref = "messageBadRequestRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @Parameter(in = ParameterIn.HEADER, description = "Bearer Token String Required", name = "Authorization")
    @PostMapping("/messages")
    public ResponseEntity<?> messages(@RequestBody Object body) {
        // TODO: process POST request

        var response = new HashMap<String, String>();
        response.put("message", "Message send with success");
        return ResponseEntity.ok(response);
    }

}
