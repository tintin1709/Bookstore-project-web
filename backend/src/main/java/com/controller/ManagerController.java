package com.example.bookstore.controller;

import com.example.bookstore.model.Author;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;
import com.example.bookstore.model.Coupon;
import com.example.bookstore.repository.AuditLogRepository;
import com.example.bookstore.repository.CatalogRepository;
import com.example.bookstore.repository.OrderRepository;
import com.example.bookstore.repository.ReservationRepository;
import com.example.bookstore.service.CurrentUserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    private final CatalogRepository catalog; private final OrderRepository orders; private final ReservationRepository reservations; private final AuditLogRepository audit; private final CurrentUserService current;
    public ManagerController(CatalogRepository catalog, OrderRepository orders, ReservationRepository reservations, AuditLogRepository audit, CurrentUserService current) { this.catalog=catalog;this.orders=orders;this.reservations=reservations;this.audit=audit;this.current=current; }

    @GetMapping("/books") public String books(@RequestParam(required=false) String q, @RequestParam(required=false) Long categoryId, @RequestParam(required=false) String status, @RequestParam(required=false) BigDecimal minPrice, @RequestParam(required=false) BigDecimal maxPrice, @RequestParam(required=false) Integer minStock, @RequestParam(defaultValue="title") String sort, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="20") int size, Model model) {
        model.addAttribute("pageResult", catalog.searchBooks(q, categoryId, status, minPrice, maxPrice, minStock, sort, page, size)); model.addAttribute("categories", catalog.categories()); model.addAttribute("q",q); model.addAttribute("status",status); return "manager-books";
    }
    @GetMapping("/books/new") public String newBook(Model model) { model.addAttribute("book", new Book()); model.addAttribute("categories", catalog.categories()); return "book-form"; }
    @GetMapping("/books/{id}/edit") public String edit(@PathVariable Long id, Model model) { model.addAttribute("book", catalog.findBook(id).orElseThrow()); model.addAttribute("categories", catalog.categories()); return "book-form"; }
    @PostMapping("/books") public String save(@ModelAttribute Book book, Authentication auth) { Long id = catalog.saveBook(book, current.current(auth).getId()); audit.log(current.current(auth).getId(),"BOOK",id,"SAVE",null,book.getTitle()); return "redirect:/manager/books"; }
    @PostMapping("/books/{id}/delete") public String delete(@PathVariable Long id, Authentication auth) { catalog.softDeleteBook(id); audit.log(current.current(auth).getId(),"BOOK",id,"SOFT_DELETE",null,"INACTIVE"); return "redirect:/manager/books"; }

    @GetMapping("/categories") public String categories(Model model) { model.addAttribute("categories", catalog.categories()); model.addAttribute("category", new Category()); return "categories"; }
    @PostMapping("/categories") public String saveCategory(@ModelAttribute Category category) { catalog.saveCategory(category); return "redirect:/manager/categories"; }
    @GetMapping("/authors") public String authors(Model model) { model.addAttribute("authors", catalog.authors()); model.addAttribute("author", new Author()); return "authors"; }
    @PostMapping("/authors") public String saveAuthor(@ModelAttribute Author author) { catalog.saveAuthor(author); return "redirect:/manager/authors"; }
    @GetMapping("/coupons") public String coupons(Model model) { model.addAttribute("coupons", catalog.coupons()); model.addAttribute("coupon", new Coupon()); return "coupons"; }
    @PostMapping("/coupons") public String saveCoupon(@RequestParam String code, @RequestParam String discountType, @RequestParam BigDecimal discountValue, @RequestParam BigDecimal minOrderAmount, @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime startsAt, @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime endsAt, @RequestParam String status) { Coupon c=new Coupon(); c.setCode(code); c.setDiscountType(discountType); c.setDiscountValue(discountValue); c.setMinOrderAmount(minOrderAmount); c.setStartsAt(startsAt); c.setEndsAt(endsAt); c.setStatus(status); catalog.saveCoupon(c); return "redirect:/manager/coupons"; }
    @GetMapping("/orders") public String allOrders(Model model) { model.addAttribute("orders", orders.findOrders(null, true)); return "manager-orders"; }
    @PostMapping("/orders/{id}/status") public String status(@PathVariable Long id, @RequestParam String status, @RequestParam String paymentStatus, @RequestParam String shipmentStatus) { orders.updateStatus(id,status,paymentStatus,shipmentStatus); return "redirect:/manager/orders"; }
    @GetMapping("/reservations") public String allReservations(Model model) { model.addAttribute("reservations", reservations.findAll()); return "manager-reservations"; }
    @PostMapping("/reservations/{id}") public String resStatus(@PathVariable Long id, @RequestParam String status) { reservations.updateStatus(id,status); return "redirect:/manager/reservations"; }
    @GetMapping("/export/books.csv") public ResponseEntity<String> exportBooks() { var page = catalog.searchBooks(null,null,null,null,null,null,"title",0,100); StringBuilder sb=new StringBuilder("sku,title,category,price,stock,status\n"); page.getItems().forEach(b -> sb.append(b.getSku()).append(',').append('"').append(b.getTitle()).append('"').append(',').append(b.getCategoryName()).append(',').append(b.getListPrice()).append(',').append(b.getStockOnHand()).append(',').append(b.getStatus()).append('\n')); return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=books.csv").contentType(MediaType.TEXT_PLAIN).body(sb.toString()); }
}
