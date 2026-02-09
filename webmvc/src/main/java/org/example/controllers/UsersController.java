package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dtos.user.UserRegisterDTO;
import org.example.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
