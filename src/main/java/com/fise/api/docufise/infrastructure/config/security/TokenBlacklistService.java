package com.fise.api.docufise.infrastructure.config.security;

import com.fise.api.docufise.domain.model.TokenBlacklist;
import com.fise.api.docufise.domain.repository.ITokenBlacklistRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class TokenBlacklistService {
    
    private final ITokenBlacklistRepository tokenBlacklistRepository;
    
    public TokenBlacklistService(ITokenBlacklistRepository tokenBlacklistRepository) {
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }
    
    public boolean isTokenRevoked(String token) {
        return tokenBlacklistRepository.isTokenRevoked(token);
    }
    
    @Scheduled(fixedRate = 3600000)
    public void cleanupExpiredTokens() {
        tokenBlacklistRepository.deleteExpiredTokens();
    }
}