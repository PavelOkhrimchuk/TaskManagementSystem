package org.ohrim.taskmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.ohrim.taskmanagementsystem.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AuthenticationService authenticationService;


    @PostMapping("/promote")
    public ResponseEntity<Void> promoteToAdmin(@RequestParam String email) {
        authenticationService.promoteToAdmin(email);
        return ResponseEntity.ok().build();
    }
}
