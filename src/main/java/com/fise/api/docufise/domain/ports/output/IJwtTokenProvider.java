package com.fise.api.docufise.domain.ports.output;

import java.time.LocalDateTime;

public interface IJwtTokenProvider {
    String generateToken(String username);
    String getUsernameFromToken(String token);
    boolean validateToken(String token);
    LocalDateTime getExpirationDate(String token);
}