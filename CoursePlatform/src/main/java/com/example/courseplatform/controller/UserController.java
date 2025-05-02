package com.example.courseplatform.controller;

import com.example.courseplatform.model.User;
import com.example.courseplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public String viewProfile(@AuthenticationPrincipal UserDetails principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userRepository.findByEmail(principal.getUsername());
        model.addAttribute("user", user);
        return "user/profile";
    }
    
    @GetMapping("/profile/edit")
    public String editProfile(@AuthenticationPrincipal UserDetails principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userRepository.findByEmail(principal.getUsername());
        model.addAttribute("user", user);
        return "user/edit";
    }
    
    @PostMapping("/profile/update")
    public String updateProfile(@AuthenticationPrincipal UserDetails principal,
                              @ModelAttribute("user") User updatedUser) {
        if (principal == null) {
            return "redirect:/login";
        }
        
        User existingUser = userRepository.findByEmail(principal.getUsername());
        existingUser.setName(updatedUser.getName());
        userRepository.save(existingUser);
        
        return "redirect:/profile?updated=true";
    }
}
