package org.example.dtos.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserRegisterDTO {
    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private String password;

    private MultipartFile image;

    @NotNull
    private String confirmPassword;
}
