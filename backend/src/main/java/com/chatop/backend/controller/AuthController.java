package com.chatop.backend.controller;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.backend.dto.LoginDto;
import com.chatop.backend.dto.RegisterDto;
import com.chatop.backend.dto.UserResponseDto;
import com.chatop.backend.entity.User;
import com.chatop.backend.repository.UserRepository;
import com.chatop.backend.service.JwtService;
import com.chatop.backend.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Authentication")
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * @param registerDto registerDto
     * @param result      result
     * @return ResponseEntity
     */
    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "authSuccessRequestApi"),
            @ApiResponse(responseCode = "400", ref = "registerBadRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterDto registerDto,
            BindingResult result) {

        if (result.hasErrors()) {
            HashMap<String, String> errorsMap = this.getErrors(result);
            return ResponseEntity.badRequest().body(errorsMap);
        }

        var bcryptEncoder = new BCryptPasswordEncoder();
        var hashedPassword = bcryptEncoder.encode(registerDto.getPassword());

        User user = new User();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(hashedPassword);
        user.setCreated_at(new Date());
        user.setUpdated_at(new Date());
        var response = new HashMap<String, Object>();

        try {
            userService.saveUser(user);
            String jwtToken = jwtService.generateJwtToken(user);
            response.put("token", jwtToken);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("there was an error while saving in db");
        }
        return ResponseEntity.badRequest().body("An unexpected error occured");
    }

    /**
     * @param loginDto loginDto
     * @param result   result
     * @return ResponseEntity
     */
    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "authSuccessRequestApi"),
            @ApiResponse(responseCode = "400", ref = "loginBadRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginDto loginDto, BindingResult result) {

        if (result.hasErrors()) {
            HashMap<String, String> errorsMap = this.getErrors(result);
            return ResponseEntity.badRequest().body(errorsMap);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

            User user = userService.getUserByEmail(loginDto.getEmail());
            String jwtToken = jwtService.generateJwtToken(user);

            var response = new HashMap<String, Object>();
            response.put("token", jwtToken);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("there was an error during the login process");
        }

        return ResponseEntity.badRequest().body("An unexpected error occured");
    }

    /**
     * @param authentication auth
     * @return ResponseEntity
     */
    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "meSuccessRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @Parameter(in = ParameterIn.HEADER, description = "Bearer Token String Required", name = "Authorization")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/me")
    public ResponseEntity<Object> me(Authentication authentication) {

        var user = (User) authentication.getPrincipal();
        if (user != null) {
            UserResponseDto userToReturn = new UserResponseDto(user);
            return ResponseEntity.ok(userToReturn);
        }
        return ResponseEntity.badRequest().body("An unexpected error occured");

    }

    /**
     * @param result result
     * @return HashMap errorsMap
     */
    public HashMap<String, String> getErrors(BindingResult result) {

        var errorsList = result.getAllErrors();
        var errorsMap = new HashMap<String, String>();
        for (int i = 0; i < errorsList.size(); i++) {
            var error = (FieldError) errorsList.get(i);
            errorsMap.put(error.getField(), error.getDefaultMessage());
        }
        return errorsMap;
    }

}
