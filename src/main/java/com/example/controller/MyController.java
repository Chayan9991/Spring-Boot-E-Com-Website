package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MyController {

    @GetMapping("/")
    public String home(){
        return "home";
    }
    @GetMapping("/shop")
    public String shop(){
        return "shop";
    }


    @GetMapping("/login")
    public String login(@RequestParam(required = false, defaultValue = "false") boolean isAdmin, Model model){
        // Set the isAdmin attribute in the model
        model.addAttribute("isAdmin", isAdmin);
        return "login";
    }
    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/view-product")
    public String product(){
        return "view-product";
    }
}
