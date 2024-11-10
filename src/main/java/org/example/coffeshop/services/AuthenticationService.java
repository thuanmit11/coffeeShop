package org.example.coffeshop.services;

import org.example.coffeshop.dto.SignInRequest;
import org.example.coffeshop.dto.SignInResponse;

public interface AuthenticationService {

    SignInResponse login(SignInRequest signInRequest);
    String logout(String authHeader);
}
