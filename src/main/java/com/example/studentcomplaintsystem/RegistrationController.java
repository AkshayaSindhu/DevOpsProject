
package com.example.studentcomplaintsystem;

import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(
            UserDetailsManager userDetailsManager,
            PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerStudent(
            @RequestParam String fullName,
            @RequestParam String username,
            @RequestParam String password,
            RedirectAttributes redirectAttributes) {

        fullName = fullName.trim();
        username = username.trim();

        if (fullName.isBlank() || username.length() < 3
                || password.length() < 8) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Enter your name, a username of at least 3 characters, "
                            + "and a password of at least 8 characters."
            );
            return "redirect:/register";
        }

        if (userDetailsManager.userExists(username)) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Username already exists. Please choose another."
            );
            return "redirect:/register";
        }

        StudentAccount student = new StudentAccount(
                fullName,
                username,
                passwordEncoder.encode(password)
        );

        userDetailsManager.createUser(student);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Registration successful! You can now log in."
        );

        return "redirect:/register";
    }
}
