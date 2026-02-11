package org.example.dtos.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDTO {
    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmPassword;

    @NotBlank
    private String token;
}
