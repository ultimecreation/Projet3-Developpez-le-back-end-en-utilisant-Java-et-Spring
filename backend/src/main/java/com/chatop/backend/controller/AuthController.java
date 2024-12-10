package com.chatop.backend.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.backend.dto.JwtResponseDto;
import com.chatop.backend.dto.LoginDto;
import com.chatop.backend.dto.RegisterDto;
import com.chatop.backend.dto.UserResponseDto;
import com.chatop.backend.entity.User;
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
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * @param registerDto registerDto
     * @param result      result
     * @return JwtResponseDto
     */
    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "authSuccessRequestApi"),
            @ApiResponse(responseCode = "400", ref = "registerBadRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public JwtResponseDto register(@Valid @RequestBody RegisterDto registerDto) {

        String hashedPassword = passwordEncoder.encode(registerDto.getPassword());

        User user = new User();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(hashedPassword);
        user.setCreated_at(new Date());
        user.setUpdated_at(new Date());
        userService.saveUser(user);

        JwtResponseDto jwtResponseDto = new JwtResponseDto(jwtService.generateJwtToken(user));
        return jwtResponseDto;

    }

    /**
     * @param loginDto loginDto
     * @param result   result
     * @return JwtResponseDto
     */
    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "authSuccessRequestApi"),
            @ApiResponse(responseCode = "400", ref = "loginBadRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @PostMapping("/login")
    public JwtResponseDto login(@Valid @RequestBody LoginDto loginDto, BindingResult result) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        User user = userService.getUserByEmail(loginDto.getEmail());
        JwtResponseDto jwtResponseDto = new JwtResponseDto(jwtService.generateJwtToken(user));
        return jwtResponseDto;
    }

    /**
     * @param authentication auth
     * @return UserResponseDto
     */
    @Operation(responses = {
            @ApiResponse(responseCode = "200", ref = "meSuccessRequestApi"),
            @ApiResponse(responseCode = "401", ref = "unauthorizedRequestApi"),
    })
    @Parameter(in = ParameterIn.HEADER, description = "Bearer Token String Required", name = "Authorization")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/me")
    public UserResponseDto me(Authentication authentication) {

        var user = (User) authentication.getPrincipal();

        UserResponseDto userToReturn = new UserResponseDto(user);
        return userToReturn;
    }
}
