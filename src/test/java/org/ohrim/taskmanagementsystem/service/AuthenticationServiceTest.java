package org.ohrim.taskmanagementsystem.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("Test register user - success")
    void testRegisterUserSuccess() {

        String email = "test@example.com";
        String password = "password123";
        String name = "Test User";
        User user = User.builder()
                .email(email)
                .password("encodedPassword")
                .name(name)
                .role(Role.USER)
                .build();
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtTokenProvider.generateToken(any(User.class))).thenReturn("jwtToken");


        String token = authenticationService.register(email, password, name);


        assertNotNull(token);
        assertEquals("jwtToken", token);


        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(jwtTokenProvider).generateToken(userCaptor.capture());


        assertEquals(user.getEmail(), userCaptor.getValue().getEmail());
        assertEquals(user.getName(), userCaptor.getValue().getName());
        assertEquals(user.getRole(), userCaptor.getValue().getRole());

        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Test register user - email already taken")
    void testRegisterUserEmailAlreadyTaken() {

        String email = "test@example.com";
        String password = "password123";
        String name = "Test User";
        when(userRepository.existsByEmail(email)).thenReturn(true);


        EmailAlreadyTakenException exception = assertThrows(EmailAlreadyTakenException.class, () ->
                authenticationService.register(email, password, name));
        assertEquals("Email is already taken: " + email, exception.getMessage());
    }

    @Test
    @DisplayName("Test login user - success")
    void testLoginUserSuccess() {

        String email = "test@example.com";
        String password = "password123";
        User user = User.builder()
                .email(email)
                .password("encodedPassword")
                .role(Role.USER)
                .build();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.of(user));
        when(jwtTokenProvider.generateToken(user)).thenReturn("jwtToken");


        String token = authenticationService.login(email, password);


        assertNotNull(token);
        assertEquals("jwtToken", token);
        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Test login user - invalid credentials")
    void testLoginUserInvalidCredentials() {

        String email = "test@example.com";
        String password = "wrongPassword";
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new InvalidCredentialsException("Invalid email or password."));


        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () ->
                authenticationService.login(email, password));
        assertEquals("Invalid email or password.", exception.getMessage());
    }

    @Test
    @DisplayName("Test login user - user not found")
    void testLoginUserNotFound() {

        String email = "test@example.com";
        String password = "password123";
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.empty());


        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                authenticationService.login(email, password));
        assertEquals("User with email " + email + " not found.", exception.getMessage());
    }

}