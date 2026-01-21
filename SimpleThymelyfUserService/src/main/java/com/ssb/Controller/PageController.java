package com.ssb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // Redirect root URL to /index to avoid circular view
    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String showIndex() {
        return "index"; // JSP or HTML view name
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // login.html exists
    }


    @GetMapping("/register")
    public String register() {
        return "registration";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }
}
