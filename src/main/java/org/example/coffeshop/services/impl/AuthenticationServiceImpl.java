package org.example.coffeshop.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.coffeshop.dto.SignInRequest;
import org.example.coffeshop.dto.SignInResponse;
import org.example.coffeshop.repositories.UserRepository;
import org.example.coffeshop.services.AuthenticationService;
import org.example.coffeshop.services.JWTService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Override
    public SignInResponse login(SignInRequest signInRequest) {
        String username = signInRequest.getUsername();
        String password = signInRequest.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        return new SignInResponse(jwtService.generateToken(user));

    }

    @Override
    public String logout(String authorizationHeader) {
        SecurityContextHolder.clearContext();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            jwtService.invalidToken(jwt);

            return "You have been logged out successfully.";
        } else {
            return "No token found or token is missing.";
        }
    }
}
