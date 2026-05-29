package com.example.bookstore.controller;

import com.example.bookstore.repository.CatalogRepository;
import com.example.bookstore.repository.NotificationRepository;
import com.example.bookstore.repository.OrderRepository;
import com.example.bookstore.service.CurrentUserService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final JdbcTemplate jdbc;
    private final CatalogRepository catalog;
    private final OrderRepository orders;
    private final CurrentUserService current;
    private final NotificationRepository notifications;

    public HomeController(JdbcTemplate jdbc, CatalogRepository catalog, OrderRepository orders,
            CurrentUserService current, NotificationRepository notifications) {
        this.jdbc = jdbc;
        this.catalog = catalog;
        this.orders = orders;
        this.current = current;
        this.notifications = notifications;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("books",
                catalog.searchBooks(null, null, "ACTIVE", null, null, null, "newest", 0, 8).getItems());
        return "home";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        try {
            var user = current.current(auth);
            model.addAttribute("user", user);

            Integer bookCount = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM book",
                    Integer.class);

            Integer orderCount = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM customer_order",
                    Integer.class);

            Integer lowStock = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM book WHERE stock_on_hand <= reorder_level",
                    Integer.class);

            Integer unread = user == null ? 0 : notifications.unreadCount(user.getId());

            model.addAttribute("bookCount", bookCount == null ? 0 : bookCount);
            model.addAttribute("orderCount", orderCount == null ? 0 : orderCount);
            model.addAttribute("lowStock", lowStock == null ? 0 : lowStock);
            model.addAttribute("unread", unread == null ? 0 : unread);

            return "dashboard";
        } catch (Exception e) {
            model.addAttribute("user", null);
            model.addAttribute("bookCount", 0);
            model.addAttribute("orderCount", 0);
            model.addAttribute("lowStock", 0);
            model.addAttribute("unread", 0);
            model.addAttribute("error", "Dashboard data could not be loaded: " + e.getMessage());

            return "dashboard";
        }
    }
}
