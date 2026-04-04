package com.example.BookStore_Production.service;

import com.example.BookStore_Production.dto.AuthResponseDTO;
import com.example.BookStore_Production.dto.RegisterResponseDTO;
import com.example.BookStore_Production.entity.RefreshToken;
import com.example.BookStore_Production.entity.User;
import com.example.BookStore_Production.repository.UserRepository;
import com.example.BookStore_Production.security.JwtService;

import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.Optional;

//@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    public UserServiceImpl(JwtService jwtService, UserRepository userRepository,
                           PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                           RefreshTokenService refreshTokenService) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;

    }
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;


    @Override
    public RegisterResponseDTO registerUser(String username, String password){
        User user = new User();
        user.setUsername(username);

        String passwordEncoded = passwordEncoder.encode(password);

        user.setPassword(passwordEncoded);
        user.setRole(User.Role.USER);
        User saved = userRepository.save(user);
        return mapToUserResponse(saved);
    }

    @Override
    public AuthResponseDTO login(String username, String password){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        if(authentication.isAuthenticated()){
            User user = userRepository.findByUsername(username);
            String accessToken = jwtService.generateToken(user.getUsername(),user.getRole().name());

            Optional<RefreshToken> existing = refreshTokenService.findByUserID(user.getId());
            String refreshToken;
            if(existing.isPresent()){
                refreshToken = existing.get().getToken();
            }
            else{
                refreshToken = refreshTokenService.createRefreshToken(user);
            }

            return new AuthResponseDTO(
                    accessToken,
              refreshToken,
              user.getUsername(),
              user.getRole().name()
            );
        }

        throw  new RuntimeException("Invalid credentials");
    }

    @Override
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    @Override
    public void deleteUser(Long id){
            userRepository.deleteById(id);
    }

    public RegisterResponseDTO mapToUserResponse(User user){
        RegisterResponseDTO response = new RegisterResponseDTO();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setPassword(user.getPassword());

        return response;
    }
}
