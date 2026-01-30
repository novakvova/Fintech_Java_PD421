package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.services.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CategoryService categoryService;

    @GetMapping("/")
    public String hello(Model model) {
        var items = categoryService.getAll();
        model.addAttribute("categories", items);
        return "hello";
    }
}
