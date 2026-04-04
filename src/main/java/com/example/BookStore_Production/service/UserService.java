package com.example.BookStore_Production.service;

import com.example.BookStore_Production.dto.AuthResponseDTO;
import com.example.BookStore_Production.dto.RegisterRequestDTO;
import com.example.BookStore_Production.dto.RegisterResponseDTO;
import com.example.BookStore_Production.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserService {
    RegisterResponseDTO registerUser(String username, String password);
    AuthResponseDTO login(String username, String password);
    void deleteUser(Long id);
    User findByUsername(String username);
}
