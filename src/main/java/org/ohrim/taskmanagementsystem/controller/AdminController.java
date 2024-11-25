package org.ohrim.taskmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.ohrim.taskmanagementsystem.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;


    @PostMapping("/promote")
    public ResponseEntity<Void> promoteToAdmin(@RequestParam String email) {
        adminService.promoteToAdmin(email);
        return ResponseEntity.ok().build();
    }
}
