package com.example.bookstore.controller;

import com.example.bookstore.repository.NotificationRepository;
import com.example.bookstore.service.CurrentUserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationRepository notifications; private final CurrentUserService current;
    public NotificationController(NotificationRepository notifications, CurrentUserService current) { this.notifications=notifications; this.current=current; }
    @GetMapping public String list(Authentication auth, Model model) { model.addAttribute("notifications", notifications.findByUser(current.current(auth).getId())); return "notifications"; }
    @PostMapping("/{id}/read") public String read(@PathVariable Long id, Authentication auth) { notifications.markRead(id, current.current(auth).getId()); return "redirect:/notifications"; }
}
