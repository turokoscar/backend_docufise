package com.fise.api.docufise.infrastructure.controller;

import com.fise.api.docufise.shared.dto.ApiResponse;
import com.fise.api.docufise.shared.dto.LoginRequest;
import com.fise.api.docufise.shared.dto.LoginResponse;
import com.fise.api.docufise.application.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(authService.login(request), "Login exitoso"));
    }
}