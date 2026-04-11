package com.fise.api.docufise.infrastructure.controller;

import com.fise.api.docufise.domain.ports.input.AuthInputPort;
import com.fise.api.docufise.shared.dto.ApiResponse;
import com.fise.api.docufise.shared.dto.LoginRequest;
import com.fise.api.docufise.shared.dto.LoginResponse;
import com.fise.api.docufise.shared.dto.SesionResponse;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "Endpoints para autenticación de usuarios")
public class AuthController {
    
    private final AuthInputPort authInputPort;
    private final PasswordEncoder passwordEncoder;
    
    public AuthController(AuthInputPort authInputPort, PasswordEncoder passwordEncoder) {
        this.authInputPort = authInputPort;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Operation(summary = "Generar hash BCrypt", description = "Endpoint temporal para generar hashes BCrypt")
    @GetMapping("/hash/{password}")
    public ResponseEntity<ApiResponse<String>> generateHash(@PathVariable String password) {
        String hash = passwordEncoder.encode(password);
        return ResponseEntity.ok(ApiResponse.ok(hash, "Hash generado"));
    }
    
    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y retorna token JWT")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login exitoso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "423", description = "Usuario bloqueado")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        LoginResponse response = authInputPort.login(request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Login exitoso"));
    }
    
    @Operation(summary = "Cerrar sesión", description = "Invalida el token actual agregándolo a la blacklist")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sesión cerrada exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Token inválido")
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String authorization) {
        authInputPort.logout(authorization);
        return ResponseEntity.ok(ApiResponse.ok(null, "Sesión cerrada correctamente"));
    }
    
    @Operation(summary = "Refresh token", description = "Renueva el token de acceso con uno nuevo")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Token renovado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Token inválido o expirado")
    })
@PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(@RequestHeader("Authorization") String authorization) {
        LoginResponse response = authInputPort.refreshToken(authorization);
        return ResponseEntity.ok(ApiResponse.ok(response, "Token renewable correctamente"));
    }
    
    @Operation(summary = "Obtener sesiones activas", description = "Lista todas las sesiones activas del usuario")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sesiones obtenidas"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/sesiones")
    public ResponseEntity<ApiResponse<List<SesionResponse>>> getSesiones(@RequestHeader("Authorization") String authorization) {
        List<SesionResponse> sesiones = authInputPort.getSesionesActivas(authorization);
        return ResponseEntity.ok(ApiResponse.ok(sesiones, "Sesiones activas"));
    }
    
    @Operation(summary = "Cerrar sesión específica", description = "Cierra una sesión específica del usuario")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sesión cerrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @DeleteMapping("/sesiones/{sesionId}")
    public ResponseEntity<ApiResponse<Void>> cerrarSesion(@PathVariable Integer sesionId, @RequestHeader("Authorization") String authorization) {
        authInputPort.cerrarSesion(sesionId, authorization);
        return ResponseEntity.ok(ApiResponse.ok(null, "Sesión cerrada"));
    }
    
    @Operation(summary = "Cerrar todas las sesiones", description = "Cierra todas las sesiones activas del usuario")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Todas las sesiones cerradas"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @DeleteMapping("/sesiones")
    public ResponseEntity<ApiResponse<Void>> cerrarTodasSesiones(@RequestHeader("Authorization") String authorization) {
        authInputPort.cerrarTodasLasSesiones(authorization);
        return ResponseEntity.ok(ApiResponse.ok(null, "Todas las sesiones cerradas"));
    }
}