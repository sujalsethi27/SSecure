package com.project2.mailjwt.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/view/login")
    public String login() {
        return "login";
    }
    @GetMapping("/view/register")
public String registerPage() {
    return "register";
}
@GetMapping("/view/verifyEmail")
public String verifyEmailPage() {
    return "verifyEmail";
}
@GetMapping("/view/forgotPassword")
public String forgotPasswordPage() {
    return "forgotPassword";
}
@GetMapping("/view/resetPassword")
public String resetPasswordPage() {
    return "resetPassword";
}
@GetMapping("/view/dashboard")
public String dashboard(Authentication authentication) {

    if (authentication == null) {
        System.out.println("Authentication is NULL");
        return "redirect:/view/login";
    }

    System.out.println("Logged in user: " + authentication.getName());

    return "dashboard";
}
}