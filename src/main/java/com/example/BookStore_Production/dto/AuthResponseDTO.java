package com.example.BookStore_Production.dto;

public class AuthResponseDTO {
    private String accessToken;
    private String refreshToken;
    private String username;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    private String role;
    public AuthResponseDTO(String accessToken, String refreshToken, String username, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.username = username;
        this.role = role;
    }

}
