package com.ssb.Controller;

import com.ssb.Entity.User;
import com.ssb.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class PageController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String showIndex() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            Principal principal,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            Model model
    ) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update user details
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        try {
            userRepository.save(user);
            model.addAttribute("success", "Profile updated successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update profile. Try again.");
        }

        model.addAttribute("user", user);
        return "profile";
    }
}
