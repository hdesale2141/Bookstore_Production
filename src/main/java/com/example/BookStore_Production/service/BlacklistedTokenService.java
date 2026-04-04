package com.example.BookStore_Production.service;

import com.example.BookStore_Production.entity.BlacklistedToken;
import com.example.BookStore_Production.repository.BlacklistedTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BlacklistedTokenService {

    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public BlacklistedTokenService(BlacklistedTokenRepository blacklistedTokenRepository) {
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    public void blacklist(String token){
        BlacklistedToken t = new BlacklistedToken();
        t.setToken(token);
        t.setBlacklistedAt(LocalDateTime.now());
        blacklistedTokenRepository.save(t);
    }

    public boolean isBlacklisted(String token){
        return blacklistedTokenRepository.existsByToken(token);
    }
}
