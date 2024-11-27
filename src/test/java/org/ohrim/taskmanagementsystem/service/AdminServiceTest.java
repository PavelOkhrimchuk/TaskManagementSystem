package org.ohrim.taskmanagementsystem.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ohrim.taskmanagementsystem.entity.User;
import org.ohrim.taskmanagementsystem.entity.user.Role;
import org.ohrim.taskmanagementsystem.exception.user.UserNotFoundException;
import org.ohrim.taskmanagementsystem.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    @DisplayName("Test promote user to admin - success")
    void testPromoteToAdminSuccess() {

        String email = "test@example.com";
        User user = User.builder()
                .email(email)
                .role(Role.USER)
                .build();
        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);


        adminService.promoteToAdmin(email);


        assertEquals(Role.ADMIN, user.getRole());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Test promote user to admin - user not found")
    void testPromoteToAdminUserNotFound() {

        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.empty());


        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> adminService.promoteToAdmin(email));
        assertEquals("User with email " + email + " not found.", exception.getMessage());
    }

    @Test
    @DisplayName("Test demote admin to user - success")
    void testDemoteToUserSuccess() {

        String email = "admin@example.com";
        User user = User.builder()
                .email(email)
                .role(Role.ADMIN)
                .build();
        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);


        adminService.demoteToUser(email);


        assertEquals(Role.USER, user.getRole());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Test demote admin to user - user not found")
    void testDemoteToUserUserNotFound() {

        String email = "admin@example.com";
        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.empty());


        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> adminService.demoteToUser(email));
        assertEquals("User with email " + email + " not found.", exception.getMessage());
    }

}