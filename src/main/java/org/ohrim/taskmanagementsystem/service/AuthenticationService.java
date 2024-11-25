package org.ohrim.taskmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.ohrim.taskmanagementsystem.configuration.security.JwtTokenProvider;
import org.ohrim.taskmanagementsystem.entity.User;
import org.ohrim.taskmanagementsystem.entity.user.Role;
import org.ohrim.taskmanagementsystem.exception.user.EmailAlreadyTakenException;
import org.ohrim.taskmanagementsystem.exception.user.InvalidCredentialsException;
import org.ohrim.taskmanagementsystem.exception.user.UserNotFoundException;
import org.ohrim.taskmanagementsystem.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public String register(String email, String password, String name) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyTakenException("Email is already taken: " + email);
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .role(Role.USER)
                .createdAt(Instant.now())
                .build();

        userRepository.save(user);
        return jwtTokenProvider.generateToken(user);
    }

    public String login(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (Exception ex) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found."));

        return jwtTokenProvider.generateToken(user);
    }


}
