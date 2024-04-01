package com.example.controller;

import com.example.entity.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MyController {

    @GetMapping("/")
    public String home(){
        return "home";
    }
    @GetMapping("/shop")
    public String shop(Model model){
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "/images/cake.jpg", "Pistachio Cake", 999,"No Description"));
        products.add(new Product(2, "/images/vanillaCake.jpg", "Vanilla Cake", 675,"No Description"));
        products.add(new Product(3, "/images/belaBiscuits.jpg", "Bella Biscuits", 50,"No Description"));
        products.add(new Product(4, "/images/breadSandwich.jpg", "Bread Sandwich", 45,"No Description"));

        model.addAttribute("products", products);
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
