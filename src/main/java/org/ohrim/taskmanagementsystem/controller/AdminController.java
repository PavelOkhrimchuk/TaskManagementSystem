package org.ohrim.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.ohrim.taskmanagementsystem.dto.ErrorResponse;
import org.ohrim.taskmanagementsystem.dto.SuccessResponse;
import org.ohrim.taskmanagementsystem.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "APIs for managing administrative actions such as role promotions and demotions")

public class AdminController {

    private final AdminService adminService;


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/promote")
    @Operation(
            summary = "Promote user to admin",
            description = "Promotes a user with the specified email to admin role"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully promoted to admin",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )

    })
    public ResponseEntity<SuccessResponse> promoteToAdmin(@RequestParam String email) {
        adminService.promoteToAdmin(email);
        return ResponseEntity.ok(new SuccessResponse("User with email " + email + " successfully promoted to admin."));
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/demote")
    @Operation(
            summary = "Demote admin to user",
            description = "Demotes an admin with the specified email to user role"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Admin successfully demoted to user",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<SuccessResponse> demoteToUser(@RequestParam String email) {
        adminService.demoteToUser(email);
        return ResponseEntity.ok(new SuccessResponse("Admin with email " + email + " successfully demoted to user."));
    }
}
