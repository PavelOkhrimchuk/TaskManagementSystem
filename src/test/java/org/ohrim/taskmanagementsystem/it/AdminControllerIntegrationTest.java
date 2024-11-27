package org.ohrim.taskmanagementsystem.it;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ohrim.taskmanagementsystem.dto.auth.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminControllerIntegrationTest extends TestcontainersConfiguration{

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE users RESTART IDENTITY CASCADE");
    }

    @Test
    @DisplayName("Test promoting a user to admin")
    @WithMockUser(roles = "ADMIN")
    void testPromoteUserToAdmin_Success() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("user@example.com");
        registerRequest.setPassword("Password123");
        registerRequest.setName("User");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "user@example.com",
                                  "password": "Password123",
                                  "name": "User"
                                }
                                """))
                .andExpect(status().isOk());


        mockMvc.perform(post("/admin/promote")
                        .param("email", "user@example.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.message").value("User with email user@example.com successfully promoted to admin."));
    }

    @Test
    @DisplayName("Test demoting an admin to user")
    @WithMockUser(roles = "ADMIN")
    void testDemoteAdminToUser_Success() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("admin@example.com");
        registerRequest.setPassword("Password123");
        registerRequest.setName("Admin");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "admin@example.com",
                                  "password": "Password123",
                                  "name": "Admin"
                                }
                                """))
                .andExpect(status().isOk());


        mockMvc.perform(post("/admin/demote")
                        .param("email", "admin@example.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.message").value("Admin with email admin@example.com successfully demoted to user."));
    }

    @Test
    @DisplayName("Test promoting a non-existent user")
    @WithMockUser(roles = "ADMIN")
    void testPromoteNonExistentUser_Failure() throws Exception {
        mockMvc.perform(post("/admin/promote")
                        .param("email", "nonexistent@example.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.message").value("User with email nonexistent@example.com not found."));
    }

    @Test
    @DisplayName("Test demoting a non-existent admin")
    @WithMockUser(roles = "ADMIN")
    void testDemoteNonExistentAdmin_Failure() throws Exception {
        mockMvc.perform(post("/admin/demote")
                        .param("email", "nonexistentadmin@example.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.message").value("User with email nonexistentadmin@example.com not found."));
    }

}
