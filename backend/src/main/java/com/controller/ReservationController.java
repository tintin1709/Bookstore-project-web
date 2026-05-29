package com.example.bookstore.controller;

import com.example.bookstore.repository.ReservationRepository;
import com.example.bookstore.service.CurrentUserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationRepository reservations; private final CurrentUserService current;
    public ReservationController(ReservationRepository reservations, CurrentUserService current) { this.reservations=reservations; this.current=current; }
    @PostMapping("/book/{bookId}") public String create(@PathVariable Long bookId, @RequestParam(defaultValue="1") int quantity, Authentication auth, RedirectAttributes ra) { reservations.createReservation(current.current(auth).getId(), bookId, quantity); ra.addFlashAttribute("success","Reservation request submitted."); return "redirect:/reservations"; }
    @GetMapping public String mine(Authentication auth, Model model) { model.addAttribute("reservations", reservations.findByUser(current.current(auth).getId())); return "reservations"; }
}
