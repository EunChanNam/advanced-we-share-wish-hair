package com.inq.wishhair.wesharewishhair.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inq.wishhair.wesharewishhair.auth.config.resolver.ExtractPayload;
import com.inq.wishhair.wesharewishhair.auth.controller.dto.request.LoginRequest;
import com.inq.wishhair.wesharewishhair.auth.service.AuthService;
import com.inq.wishhair.wesharewishhair.auth.service.dto.response.LoginResponse;
import com.inq.wishhair.wesharewishhair.global.dto.response.Success;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest.getEmail(), loginRequest.getPw());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Success> logout(@ExtractPayload Long userId) {
        authService.logout(userId);
        return ResponseEntity.ok(new Success());
    }
}
