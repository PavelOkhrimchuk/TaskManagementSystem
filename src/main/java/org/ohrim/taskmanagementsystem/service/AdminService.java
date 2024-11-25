package org.ohrim.taskmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.ohrim.taskmanagementsystem.entity.User;
import org.ohrim.taskmanagementsystem.entity.user.Role;
import org.ohrim.taskmanagementsystem.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public void promoteToAdmin(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }
}
