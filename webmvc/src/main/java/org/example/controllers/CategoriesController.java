package org.example.controllers;


import lombok.RequiredArgsConstructor;
import org.example.services.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoriesController {
    private final CategoryService categoryService;

    @GetMapping("/list")
    public String list(Model model){
        var items = categoryService.getAll();
        model.addAttribute("categories", items);
        return "categories/list";
    }
}
