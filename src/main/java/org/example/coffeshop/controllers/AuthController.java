package org.example.coffeshop.controllers;

import lombok.RequiredArgsConstructor;
import org.example.coffeshop.dto.SignInRequest;
import org.example.coffeshop.dto.SignInResponse;
import org.example.coffeshop.services.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("auth/login")
    public ResponseEntity<SignInResponse> login(@RequestBody SignInRequest signInRequest) {
        var response = authenticationService.login(signInRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        return new ResponseEntity<>(authenticationService.logout(authorizationHeader), HttpStatus.OK);
    }
}
