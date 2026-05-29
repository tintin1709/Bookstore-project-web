package com.example.bookstore.controller;

import com.example.bookstore.repository.CatalogRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class CatalogController {
    private final CatalogRepository catalog;
    public CatalogController(CatalogRepository catalog) { this.catalog = catalog; }

    @GetMapping("/catalog")
    public String catalog(@RequestParam(required=false) String q, @RequestParam(required=false) Long categoryId,
                          @RequestParam(required=false) BigDecimal minPrice, @RequestParam(required=false) BigDecimal maxPrice,
                          @RequestParam(required=false) Integer minStock, @RequestParam(defaultValue="title") String sort,
                          @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size, Model model) {
        size = Math.min(Math.max(size, 10), 100);
        model.addAttribute("pageResult", catalog.searchBooks(q, categoryId, "ACTIVE", minPrice, maxPrice, minStock, sort, page, size));
        model.addAttribute("categories", catalog.categories());
        model.addAttribute("q", q); model.addAttribute("categoryId", categoryId); model.addAttribute("minPrice", minPrice); model.addAttribute("maxPrice", maxPrice); model.addAttribute("minStock", minStock); model.addAttribute("sort", sort); model.addAttribute("size", size);
        return "catalog";
    }

    @GetMapping("/catalog/{id}")
    public String details(@PathVariable Long id, Model model) {
        model.addAttribute("book", catalog.findBook(id).orElseThrow());
        return "book-details";
    }
}
