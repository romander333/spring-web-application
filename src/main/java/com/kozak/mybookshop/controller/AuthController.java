package com.kozak.mybookshop.controller;

import com.kozak.mybookshop.dto.user.UserLoginRequestDto;
import com.kozak.mybookshop.dto.user.UserLoginResponseDto;
import com.kozak.mybookshop.dto.user.UserRegistrationRequestDto;
import com.kozak.mybookshop.dto.user.UserResponseDto;
import com.kozak.mybookshop.security.AuthenticationService;
import com.kozak.mybookshop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User management", description = "Endpoints for managing users")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "User login",
            description =
                    "Authenticates a user by email and password and returns a JWT access token.")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/registration")
    @Operation(summary = "Register", description = "Register User")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto requestDto) {
        return userService.registration(requestDto);
    }
}
