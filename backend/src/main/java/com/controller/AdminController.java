package com.example.bookstore.controller;

import com.example.bookstore.repository.AuditLogRepository;
import com.example.bookstore.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserRepository users; private final AuditLogRepository audit;
    public AdminController(UserRepository users, AuditLogRepository audit) { this.users=users; this.audit=audit; }
    @GetMapping("/users") public String users(@RequestParam(required=false) String q, Model model) { model.addAttribute("users", users.findAll(q)); model.addAttribute("q", q); return "admin-users"; }
    @PostMapping("/users/{id}") public String update(@PathVariable Long id, @RequestParam String status, @RequestParam String role) { users.updateStatusAndRole(id, status, role); audit.log(null,"USER",id,"UPDATE_ROLE_STATUS",null,status+"/"+role); return "redirect:/admin/users"; }
    @GetMapping("/audit") public String audit(Model model) { model.addAttribute("logs", audit.latest()); return "audit"; }
}
