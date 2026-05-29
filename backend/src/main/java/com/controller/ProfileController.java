package com.example.bookstore.controller;

import com.example.bookstore.repository.UserRepository;
import com.example.bookstore.service.CurrentUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final CurrentUserService current; private final UserRepository users; private final PasswordEncoder encoder;
    public ProfileController(CurrentUserService current, UserRepository users, PasswordEncoder encoder) { this.current=current; this.users=users; this.encoder=encoder; }
    @GetMapping public String profile(Authentication auth, Model model) { model.addAttribute("user", current.current(auth)); return "profile"; }
    @PostMapping("/update") public String update(@RequestParam String fullName, @RequestParam String phone, Authentication auth, RedirectAttributes ra) { users.updateProfile(current.current(auth).getId(), fullName, phone); ra.addFlashAttribute("success", "Profile updated."); return "redirect:/profile"; }
    @PostMapping("/password") public String password(@RequestParam String newPassword, Authentication auth, RedirectAttributes ra) { if(newPassword.length()<8){ra.addFlashAttribute("error","Password must contain at least 8 characters."); return "redirect:/profile";} users.changePassword(current.current(auth).getId(), encoder.encode(newPassword)); ra.addFlashAttribute("success", "Password changed."); return "redirect:/profile"; }
}
