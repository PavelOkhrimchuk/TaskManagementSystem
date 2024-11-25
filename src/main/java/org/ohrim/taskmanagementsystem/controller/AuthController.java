package org.ohrim.taskmanagementsystem.controller;


import lombok.RequiredArgsConstructor;
import org.ohrim.taskmanagementsystem.dto.auth.LoginRequest;
import org.ohrim.taskmanagementsystem.dto.auth.RegisterRequest;
import org.ohrim.taskmanagementsystem.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        String token = authenticationService.register(request.getEmail(), request.getPassword(), request.getName());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = authenticationService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(token);
    }
}
