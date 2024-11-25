package org.ohrim.taskmanagementsystem.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequest {


    @NotBlank(message = "Content must not be blank.")
    @Size(min = 1, max = 1000, message = "Content must be between 1 and 1000 characters.")
    private String content;
}
