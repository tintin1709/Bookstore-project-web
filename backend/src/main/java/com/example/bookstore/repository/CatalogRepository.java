package com.example.bookstore.repository;

import com.example.bookstore.dto.PageResult;
import com.example.bookstore.model.Author;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;
import com.example.bookstore.model.Coupon;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CatalogRepository {
    private final JdbcTemplate jdbc;
    public CatalogRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public PageResult<Book> searchBooks(String q, Long categoryId, String status, BigDecimal minPrice, BigDecimal maxPrice, Integer minStock, String sort, int page, int size) {
        List<Object> params = new ArrayList<>();
        StringBuilder where = new StringBuilder(" WHERE 1=1 ");
        if (q != null && !q.isBlank()) { where.append(" AND (LOWER(b.title) LIKE ? OR LOWER(b.sku) LIKE ? OR LOWER(b.isbn13) LIKE ? OR LOWER(b.publisher) LIKE ?) "); String like="%"+q.toLowerCase()+"%"; params.add(like);params.add(like);params.add(like);params.add(like); }
        if (categoryId != null) { where.append(" AND b.category_id=? "); params.add(categoryId); }
        if (status != null && !status.isBlank()) { where.append(" AND b.status=? "); params.add(status); }
        if (minPrice != null) { where.append(" AND b.list_price>=? "); params.add(minPrice); }
        if (maxPrice != null) { where.append(" AND b.list_price<=? "); params.add(maxPrice); }
        if (minStock != null) { where.append(" AND b.stock_on_hand>=? "); params.add(minStock); }
        String order = switch (sort == null ? "title" : sort) {
            case "price_desc" -> " b.list_price DESC ";
            case "price_asc" -> " b.list_price ASC ";
            case "stock" -> " b.stock_on_hand ASC ";
            case "newest" -> " b.created_at DESC ";
            default -> " b.title ASC ";
        };
        String base = " FROM book b JOIN category c ON c.id=b.category_id " + where;
        long total = jdbc.queryForObject("SELECT COUNT(*) " + base, Long.class, params.toArray());
        params.add(size); params.add(page * size);
        String sql = "SELECT b.*, c.name category_name, " +
                "(SELECT COALESCE(GROUP_CONCAT(a.name), '') FROM author a JOIN book_author ba ON a.id=ba.author_id WHERE ba.book_id=b.id) authors " +
                base + " ORDER BY " + order + " LIMIT ? OFFSET ?";
        return new PageResult<>(jdbc.query(sql, RowMappers.BOOK, params.toArray()), page, size, total);
    }

    public Optional<Book> findBook(Long id) {
        List<Book> list = jdbc.query("SELECT b.*, c.name category_name, (SELECT COALESCE(GROUP_CONCAT(a.name), '') FROM author a JOIN book_author ba ON a.id=ba.author_id WHERE ba.book_id=b.id) authors FROM book b JOIN category c ON c.id=b.category_id WHERE b.id=?", RowMappers.BOOK, id);
        return list.stream().findFirst();
    }

    public Long saveBook(Book b, Long actorId) {
        if (b.getId() == null) {
            var kh = new org.springframework.jdbc.support.GeneratedKeyHolder();
            jdbc.update(con -> { PreparedStatement ps = con.prepareStatement("INSERT INTO book(category_id,sku,isbn13,title,description,publisher,publication_year,language,list_price,stock_on_hand,reorder_level,status,image_url,created_by,updated_by) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1,b.getCategoryId());ps.setString(2,b.getSku());ps.setString(3,b.getIsbn13());ps.setString(4,b.getTitle());ps.setString(5,b.getDescription());ps.setString(6,b.getPublisher());ps.setObject(7,b.getPublicationYear());ps.setString(8,b.getLanguage());ps.setBigDecimal(9,b.getListPrice());ps.setInt(10,b.getStockOnHand());ps.setInt(11,b.getReorderLevel());ps.setString(12,b.getStatus());ps.setString(13,b.getImageUrl());ps.setObject(14,actorId);ps.setObject(15,actorId); return ps; }, kh);
            return kh.getKey().longValue();
        }
        jdbc.update("UPDATE book SET category_id=?, sku=?, isbn13=?, title=?, description=?, publisher=?, publication_year=?, language=?, list_price=?, stock_on_hand=?, reorder_level=?, status=?, image_url=?, updated_by=?, updated_at=CURRENT_TIMESTAMP WHERE id=?", b.getCategoryId(), b.getSku(), b.getIsbn13(), b.getTitle(), b.getDescription(), b.getPublisher(), b.getPublicationYear(), b.getLanguage(), b.getListPrice(), b.getStockOnHand(), b.getReorderLevel(), b.getStatus(), b.getImageUrl(), actorId, b.getId());
        return b.getId();
    }

    public void softDeleteBook(Long id) { jdbc.update("UPDATE book SET status='INACTIVE', updated_at=CURRENT_TIMESTAMP WHERE id=?", id); }
    public List<Category> categories() { return jdbc.query("SELECT * FROM category ORDER BY name", RowMappers.CATEGORY); }
    public List<Author> authors() { return jdbc.query("SELECT * FROM author ORDER BY name", RowMappers.AUTHOR); }
    public List<Coupon> coupons() { return jdbc.query("SELECT * FROM coupon ORDER BY starts_at DESC", RowMappers.COUPON); }

    public Optional<Coupon> findValidCoupon(String code, BigDecimal subtotal) {
        List<Coupon> list = jdbc.query("SELECT * FROM coupon WHERE UPPER(code)=UPPER(?) AND status='ACTIVE' AND CURRENT_TIMESTAMP BETWEEN starts_at AND ends_at AND min_order_amount <= ?", RowMappers.COUPON, code, subtotal);
        return list.stream().findFirst();
    }

    public void saveCategory(Category c) {
        if (c.getId() == null) jdbc.update("INSERT INTO category(name,slug,description,status) VALUES(?,?,?,?)", c.getName(), c.getSlug(), c.getDescription(), c.getStatus());
        else jdbc.update("UPDATE category SET name=?, slug=?, description=?, status=?, updated_at=CURRENT_TIMESTAMP WHERE id=?", c.getName(), c.getSlug(), c.getDescription(), c.getStatus(), c.getId());
    }
    public void saveAuthor(Author a) {
        if (a.getId() == null) jdbc.update("INSERT INTO author(name,slug,biography,country,status) VALUES(?,?,?,?,?)", a.getName(), a.getSlug(), a.getBiography(), a.getCountry(), a.getStatus());
        else jdbc.update("UPDATE author SET name=?, slug=?, biography=?, country=?, status=?, updated_at=CURRENT_TIMESTAMP WHERE id=?", a.getName(), a.getSlug(), a.getBiography(), a.getCountry(), a.getStatus(), a.getId());
    }
    public void saveCoupon(Coupon c) {
        if (c.getId() == null) jdbc.update("INSERT INTO coupon(code,discount_type,discount_value,min_order_amount,starts_at,ends_at,status) VALUES(?,?,?,?,?,?,?)", c.getCode(), c.getDiscountType(), c.getDiscountValue(), c.getMinOrderAmount(), c.getStartsAt(), c.getEndsAt(), c.getStatus());
        else jdbc.update("UPDATE coupon SET code=?, discount_type=?, discount_value=?, min_order_amount=?, starts_at=?, ends_at=?, status=? WHERE id=?", c.getCode(), c.getDiscountType(), c.getDiscountValue(), c.getMinOrderAmount(), c.getStartsAt(), c.getEndsAt(), c.getStatus(), c.getId());
    }

    public void decreaseStock(Long bookId, int quantity) { jdbc.update("UPDATE book SET stock_on_hand=stock_on_hand-?, updated_at=CURRENT_TIMESTAMP WHERE id=? AND stock_on_hand>=?", quantity, bookId, quantity); }
    public void addInventoryTx(Long bookId, Long orderItemId, Long reservationId, String type, int delta, int balance, String note) { jdbc.update("INSERT INTO inventory_transaction(book_id,order_item_id,reservation_id,tx_type,quantity_delta,balance_after,note) VALUES(?,?,?,?,?,?,?)", bookId, orderItemId, reservationId, type, delta, balance, note); }
}
