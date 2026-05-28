package com.example.bookstore.repository;

import com.example.bookstore.model.*;
import org.springframework.jdbc.core.RowMapper;

public final class RowMappers {
    private RowMappers() {}

    public static final RowMapper<Category> CATEGORY = (rs, row) -> {
        Category c = new Category();
        c.setId(rs.getLong("id")); c.setName(rs.getString("name")); c.setSlug(rs.getString("slug"));
        c.setDescription(rs.getString("description")); c.setStatus(rs.getString("status")); return c;
    };

    public static final RowMapper<Author> AUTHOR = (rs, row) -> {
        Author a = new Author();
        a.setId(rs.getLong("id")); a.setName(rs.getString("name")); a.setSlug(rs.getString("slug"));
        a.setBiography(rs.getString("biography")); a.setCountry(rs.getString("country")); a.setStatus(rs.getString("status")); return a;
    };

    public static final RowMapper<Book> BOOK = (rs, row) -> {
        Book b = new Book();
        b.setId(rs.getLong("id")); b.setCategoryId(rs.getLong("category_id")); b.setCategoryName(rs.getString("category_name"));
        b.setSku(rs.getString("sku")); b.setIsbn13(rs.getString("isbn13")); b.setTitle(rs.getString("title"));
        b.setDescription(rs.getString("description")); b.setPublisher(rs.getString("publisher"));
        b.setPublicationYear((Integer) rs.getObject("publication_year")); b.setLanguage(rs.getString("language"));
        b.setListPrice(rs.getBigDecimal("list_price")); b.setStockOnHand(rs.getInt("stock_on_hand"));
        b.setReorderLevel(rs.getInt("reorder_level")); b.setStatus(rs.getString("status")); b.setImageUrl(rs.getString("image_url"));
        try { b.setAuthors(rs.getString("authors")); } catch (Exception ignored) { }
        return b;
    };

    public static final RowMapper<AppUser> USER = (rs, row) -> {
        AppUser u = new AppUser();
        u.setId(rs.getLong("id")); u.setEmail(rs.getString("email")); u.setPasswordHash(rs.getString("password_hash"));
        u.setFullName(rs.getString("full_name")); u.setPhone(rs.getString("phone")); u.setStatus(rs.getString("status"));
        u.setEmailVerified(rs.getBoolean("email_verified")); u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime()); return u;
    };

    public static final RowMapper<Coupon> COUPON = (rs, row) -> {
        Coupon c = new Coupon(); c.setId(rs.getLong("id")); c.setCode(rs.getString("code"));
        c.setDiscountType(rs.getString("discount_type")); c.setDiscountValue(rs.getBigDecimal("discount_value"));
        c.setMinOrderAmount(rs.getBigDecimal("min_order_amount"));
        c.setStartsAt(rs.getTimestamp("starts_at").toLocalDateTime()); c.setEndsAt(rs.getTimestamp("ends_at").toLocalDateTime());
        c.setStatus(rs.getString("status")); return c;
    };

    public static final RowMapper<Reservation> RESERVATION = (rs, row) -> {
        Reservation r = new Reservation(); r.setId(rs.getLong("id")); r.setUserId(rs.getLong("user_id"));
        r.setCustomerName(rs.getString("customer_name")); r.setBookId(rs.getLong("book_id")); r.setBookTitle(rs.getString("book_title"));
        r.setQuantity(rs.getInt("quantity")); r.setStatus(rs.getString("status")); r.setReservedAt(rs.getTimestamp("reserved_at").toLocalDateTime());
        if (rs.getTimestamp("expires_at") != null) r.setExpiresAt(rs.getTimestamp("expires_at").toLocalDateTime());
        if (rs.getTimestamp("notified_at") != null) r.setNotifiedAt(rs.getTimestamp("notified_at").toLocalDateTime());
        return r;
    };

    public static final RowMapper<OrderSummary> ORDER = (rs, row) -> {
        OrderSummary o = new OrderSummary(); o.setId(rs.getLong("id")); o.setOrderNo(rs.getString("order_no"));
        o.setCustomerName(rs.getString("customer_name")); o.setStatus(rs.getString("status"));
        o.setPaymentStatus(rs.getString("payment_status")); o.setShipmentStatus(rs.getString("shipment_status"));
        o.setGrandTotal(rs.getBigDecimal("grand_total")); o.setPlacedAt(rs.getTimestamp("placed_at").toLocalDateTime()); return o;
    };

    public static final RowMapper<Notification> NOTIFICATION = (rs, row) -> {
        Notification n = new Notification(); n.setId(rs.getLong("id")); n.setTitle(rs.getString("title"));
        n.setMessage(rs.getString("message")); n.setNotificationType(rs.getString("notification_type"));
        n.setStatus(rs.getString("status")); n.setSentAt(rs.getTimestamp("sent_at").toLocalDateTime()); return n;
    };
}
