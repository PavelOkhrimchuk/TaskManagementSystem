package org.ohrim.taskmanagementsystem.dto.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ohrim.taskmanagementsystem.entity.user.Role;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private Role role;
}
