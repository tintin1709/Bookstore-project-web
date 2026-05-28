package com.example.bookstore.service;

import com.example.bookstore.dto.CartLine;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Coupon;
import com.example.bookstore.repository.CatalogRepository;
import com.example.bookstore.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class BookstoreService {
    private final CatalogRepository catalog;
    private final OrderRepository orders;

    public BookstoreService(CatalogRepository catalog, OrderRepository orders) {
        this.catalog = catalog;
        this.orders = orders;
    }

    public BigDecimal subtotal(List<CartLine> lines) {
        return lines.stream().map(CartLine::getLineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal discount(Coupon coupon, BigDecimal subtotal) {
        if (coupon == null)
            return BigDecimal.ZERO;
        if ("PERCENT".equals(coupon.getDiscountType())) {
            return subtotal.multiply(coupon.getDiscountValue()).divide(BigDecimal.valueOf(100), 2,
                    RoundingMode.HALF_UP);
        }
        return coupon.getDiscountValue().min(subtotal);
    }

    @Transactional
    public Long placeOrder(Long userId, List<CartLine> lines, String couponCode, String address) {
        if (lines == null || lines.isEmpty())
            throw new IllegalArgumentException("Cart is empty.");
        for (CartLine line : lines) {
            Book book = catalog.findBook(line.getBookId())
                    .orElseThrow(() -> new IllegalArgumentException("Book not found."));
            if (!"ACTIVE".equals(book.getStatus()))
                throw new IllegalArgumentException(book.getTitle() + " is inactive.");
            if (book.getStockOnHand() < line.getQuantity())
                throw new IllegalArgumentException(book.getTitle() + " does not have enough stock.");
        }
        BigDecimal subtotal = subtotal(lines);
        Coupon coupon = (couponCode == null || couponCode.isBlank()) ? null
                : catalog.findValidCoupon(couponCode, subtotal).orElse(null);
        BigDecimal discount = discount(coupon, subtotal);
        BigDecimal total = subtotal.subtract(discount);
        for (CartLine line : lines)
            catalog.decreaseStock(line.getBookId(), line.getQuantity());
        return  orders.createOrder(userId, coupon == null ? null : coupon.getId(), 
                subtotal, discount, total, address, lines);
    }
}
