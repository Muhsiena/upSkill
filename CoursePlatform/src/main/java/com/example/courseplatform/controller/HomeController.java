package com.example.courseplatform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "redirect:/courses";
    }
    
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
    
    // Removed duplicate /register mapping that conflicts with AuthController
}
