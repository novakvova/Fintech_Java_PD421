package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dtos.user.ForgotPasswordDTO;
import org.example.dtos.user.ResetPasswordDTO;
import org.example.dtos.user.UserRegisterDTO;
import org.example.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    private final UserService userService;

    @GetMapping("/registration")
    public String showRegistrationForm(Model model){
        var userDTO = new UserRegisterDTO();
        model.addAttribute("user", userDTO);
        return "users/registration";
    }

    @PostMapping("/registration")
    public String save(UserRegisterDTO userDTO){
        userService.createUser(userDTO);
        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public String showLoginForm(){
        return "users/login";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordForm(Model model) {
        model.addAttribute("forgotPasswordDTO", new ForgotPasswordDTO());
        return "users/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPasswordSubmit(
            @Valid @ModelAttribute ForgotPasswordDTO forgotPasswordDTO,
            BindingResult result,
            Model model,
            HttpServletRequest request) {

        if (result.hasErrors()) {
            return "users/forgot-password";
        }

        boolean sent = userService.forgotPassword(forgotPasswordDTO, request);
        if (sent) {
            return "users/forgot-password-success";
        } else {
            model.addAttribute("error", "Користувача з таким email не знайдено.");
            return "users/forgot-password";
        }
    }

    @GetMapping("/reset-password")
    public String resetPasswordForm(@RequestParam("token") String token, Model model) {
        ResetPasswordDTO dto = new ResetPasswordDTO();
        dto.setToken(token);
        model.addAttribute("resetPasswordDTO", dto);
        return "users/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPasswordSubmit(
            @Valid @ModelAttribute ResetPasswordDTO resetPasswordDTO,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "users/reset-password";
        }

        boolean success = userService.resetPassword(resetPasswordDTO);
        if (success) {
            model.addAttribute("message", "Пароль успішно змінено! Тепер ви можете увійти.");
            return "users/login";
        } else {
            model.addAttribute("error", "Токен недійсний або паролі не співпадають.");
            return "users/reset-password";
        }
    }
}
