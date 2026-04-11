package com.fise.api.docufise.infrastructure.repository;

import com.fise.api.docufise.domain.model.TokenBlacklist;
import com.fise.api.docufise.domain.repository.ITokenBlacklistRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TokenBlacklistJpaRepository extends JpaRepository<TokenBlacklist, Integer>, ITokenBlacklistRepository {
    
    boolean existsByToken(String token);
    
    default boolean isTokenRevoked(String token) {
        return existsByToken(token);
    }
    
    @Query("SELECT t FROM TokenBlacklist t WHERE t.expiresAt < :now")
    List<TokenBlacklist> findExpiredTokens(LocalDateTime now);
    
    default void deleteExpiredTokens() {
        deleteAll(findExpiredTokens(java.time.LocalDateTime.now()));
    }
    
    @Modifying
    @Query("DELETE FROM TokenBlacklist t WHERE t.token = :token")
    void deleteByTokenValue(String token);
    
    default void deleteByToken(String token) {
        deleteByTokenValue(token);
    }
}