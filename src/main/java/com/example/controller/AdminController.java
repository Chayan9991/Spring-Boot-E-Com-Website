package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String admin(){
        return "./admin_dashboard/admin";
    }

    @GetMapping("/admin/add_product")
    public String addProduct(){
        return "./admin_dashboard/add_product";
    }

    @GetMapping("/admin/category")
    public String category(){
        return "./admin_dashboard/category";
    }
}
