package org.ohrim.taskmanagementsystem.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {


    @NotNull
    private String email;

    @NotNull
    private String password;



}
