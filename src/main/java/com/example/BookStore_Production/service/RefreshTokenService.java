package com.example.BookStore_Production.service;

import com.example.BookStore_Production.entity.RefreshToken;
import com.example.BookStore_Production.entity.User;
import com.example.BookStore_Production.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String createRefreshToken(User user){
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(LocalDateTime.now().plusDays(7));

        refreshTokenRepository.save(token);
        return token.getToken();
    }

    public RefreshToken validateRefreshToken(String token){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(()-> new RuntimeException("Invalid refresh token.."));

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())){
            RefreshToken newToken = refreshTokenRepository.findByToken(token).orElseThrow(()-> new RuntimeException("Refresh Token not found"));

            newToken.setToken(UUID.randomUUID().toString());
            newToken.setExpiryDate(LocalDateTime.now().plusDays(7));

            refreshTokenRepository.save(newToken);
            return newToken;
        }

        return refreshToken;
    }

    public Optional<RefreshToken> findByUserID(Long id){
        return refreshTokenRepository.findByUserId(id);
    }
}
