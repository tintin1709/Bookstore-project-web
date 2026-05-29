package com.example.bookstore.controller;

import com.example.bookstore.dto.CartLine;
import com.example.bookstore.repository.CatalogRepository;
import com.example.bookstore.service.BookstoreService;
import com.example.bookstore.service.CurrentUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CatalogRepository catalog; private final BookstoreService bookstore; private final CurrentUserService current;
    public CartController(CatalogRepository catalog, BookstoreService bookstore, CurrentUserService current) { this.catalog=catalog; this.bookstore=bookstore; this.current=current; }
    @SuppressWarnings("unchecked") private List<CartLine> cart(HttpSession s) { var lines=(List<CartLine>)s.getAttribute("cart"); if(lines==null){lines=new ArrayList<>();s.setAttribute("cart",lines);} return lines; }
    @PostMapping("/add/{bookId}") public String add(@PathVariable Long bookId, @RequestParam(defaultValue="1") int quantity, HttpSession session, RedirectAttributes ra) {
        var b = catalog.findBook(bookId).orElseThrow();
        var lines = cart(session);
        lines.stream().filter(x->x.getBookId().equals(bookId)).findFirst().ifPresentOrElse(x -> x.setQuantity(x.getQuantity()+quantity), () -> lines.add(new CartLine(bookId,b.getTitle(),b.getListPrice(),quantity)));
        ra.addFlashAttribute("success", "Added to cart."); return "redirect:/cart";
    }
    @GetMapping public String view(HttpSession session, Model model) { var lines=cart(session); model.addAttribute("lines", lines); model.addAttribute("subtotal", bookstore.subtotal(lines)); return "cart"; }
    @PostMapping("/remove/{bookId}") public String remove(@PathVariable Long bookId, HttpSession session) { cart(session).removeIf(l -> l.getBookId().equals(bookId)); return "redirect:/cart"; }
    @PostMapping("/checkout") public String checkout(@RequestParam String address, @RequestParam(required=false) String couponCode, HttpSession session, Authentication auth, RedirectAttributes ra) {
        try { Long orderId = bookstore.placeOrder(current.current(auth).getId(), cart(session), couponCode, address); session.removeAttribute("cart"); ra.addFlashAttribute("success", "Order placed successfully. Order ID: " + orderId); return "redirect:/orders"; }
        catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); return "redirect:/cart"; }
    }
}
