package com.example.BookStore_Production.controller;

import com.example.BookStore_Production.dto.AuthResponseDTO;
import com.example.BookStore_Production.dto.LoginRequestDTO;
import com.example.BookStore_Production.dto.RefreshRequestDTO;
import com.example.BookStore_Production.dto.RegisterRequestDTO;
import com.example.BookStore_Production.entity.RefreshToken;
import com.example.BookStore_Production.entity.User;
import com.example.BookStore_Production.security.JwtService;
import com.example.BookStore_Production.service.BlacklistedTokenService;
import com.example.BookStore_Production.service.RefreshTokenService;
import com.example.BookStore_Production.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final BlacklistedTokenService blacklistedTokenService;
    public UserController(UserService userService, RefreshTokenService refreshTokenService, JwtService jwtService, BlacklistedTokenService blacklistedTokenService) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        this.blacklistedTokenService = blacklistedTokenService;
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequestDTO request){
        userService.registerUser(
                request.getUsername(),
                request.getPassword()
        );
        return ResponseEntity.ok("User Registration Successfully....");
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDTO request){
        User user = userService.findByUsername(request.getUsername());

        AuthResponseDTO response = userService.login(
                request.getUsername(),
                request.getPassword()
        );

        return ResponseEntity.ok(response);
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer")){
            String token = authHeader.substring(7);
            blacklistedTokenService.blacklist(token);
        }
        return ResponseEntity.ok("Logged out successfully...");
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted Successfully....");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> reefreshToken(@RequestBody RefreshRequestDTO request){
        Map<String,String> map = new HashMap<>();
        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(request.getRefreshToken());

        if(!Objects.equals(refreshToken.getToken(), request.getRefreshToken())){
            map.put("message","Your old Refresh token is expired...");
            map.put("New_RefreshToken",refreshToken.getToken());

            return ResponseEntity.ok(map);
        }
        String newAccessToken = jwtService.generateToken(
                refreshToken.getUser().getUsername(),
                refreshToken.getUser().getRole().name()
        );

        return ResponseEntity.ok(Map.of("accessToken",newAccessToken));
    }
}
