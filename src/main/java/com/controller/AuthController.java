package com.controller;

import com.repository.UserRepository;
import com.service.EmailService;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Validated
public class AuthController {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final EmailService emailService;

    public AuthController(UserRepository users, PasswordEncoder encoder, EmailService emailService) {
        this.users = users;
        this.encoder = encoder;
        this.emailService = emailService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("form", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute("form") @Validated RegisterForm form,
            BindingResult result,
            RedirectAttributes ra) {

        if (!form.password.equals(form.confirmPassword)) {
            result.rejectValue("confirmPassword", "mismatch", "Password confirmation does not match.");
        }

        if (users.findByEmail(form.email).isPresent()) {
            result.rejectValue("email", "duplicate", "Email is already registered.");
        }

        if (result.hasErrors()) {
            return "register";
        }

        Long userId = users.createUser(
                form.email,
                encoder.encode(form.password),
                form.fullName,
                form.phone,
                "CUSTOMER");

        String token = UUID.randomUUID().toString();

        users.createVerificationToken(
                userId,
                token,
                LocalDateTime.now().plusHours(24));

        emailService.sendVerificationEmail(form.email, token);

        ra.addFlashAttribute(
                "success",
                "Registration successful. Please check your email to verify your account.");

        return "redirect:/login";
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, RedirectAttributes ra) {
        var userIdOpt = users.findUserIdByValidToken(token);

        if (userIdOpt.isEmpty()) {
            ra.addFlashAttribute("error", "Invalid or expired verification link.");
            return "redirect:/login";
        }

        users.markEmailVerified(userIdOpt.get(), token);

        ra.addFlashAttribute("success", "Email verified successfully. You can now log in.");
        return "redirect:/login";
    }

    public static class RegisterForm {
        @NotBlank
        @Size(min = 3, max = 100)
        public String fullName;

        @Email
        @NotBlank
        public String email;

        @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$", message = "Phone must be a valid Vietnamese phone number.")
        public String phone;

        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$", message = "Password needs 8+ chars, uppercase, lowercase, and number.")
        public String password;

        public String confirmPassword;

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String v) {
            fullName = v;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String v) {
            email = v;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String v) {
            phone = v;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String v) {
            password = v;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String v) {
            confirmPassword = v;
        }
    }
}