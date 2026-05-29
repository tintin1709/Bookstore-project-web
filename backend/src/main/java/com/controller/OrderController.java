package com.controller;

import com.repository.OrderRepository;
import com.service.CurrentUserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderRepository orders; private final CurrentUserService current;
    public OrderController(OrderRepository orders, CurrentUserService current) { this.orders=orders; this.current=current; }
    @GetMapping public String myOrders(Model model, Authentication auth) { model.addAttribute("orders", orders.findOrders(current.current(auth).getId(), false)); return "orders"; }
}
