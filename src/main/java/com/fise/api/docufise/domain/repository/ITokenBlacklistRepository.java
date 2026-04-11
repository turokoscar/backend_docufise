package com.fise.api.docufise.domain.repository;

import com.fise.api.docufise.domain.model.TokenBlacklist;
import java.time.LocalDateTime;
import java.util.List;

public interface ITokenBlacklistRepository {
    TokenBlacklist save(TokenBlacklist tokenBlacklist);
    boolean isTokenRevoked(String token);
    void deleteExpiredTokens();
    void deleteByToken(String token);
    List<TokenBlacklist> findByExpiresAtBefore(LocalDateTime dateTime);
}