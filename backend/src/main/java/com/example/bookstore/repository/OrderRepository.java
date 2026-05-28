package com.example.bookstore.repository;

import com.example.bookstore.dto.CartLine;
import com.example.bookstore.model.OrderSummary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderRepository {
    private final JdbcTemplate jdbc;
    public OrderRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public Long createOrder(Long userId, Long couponId, BigDecimal subtotal, BigDecimal discount, BigDecimal total, String address, List<CartLine> lines) {
        String orderNo = "ORD-" + System.currentTimeMillis();
        var kh = new org.springframework.jdbc.support.GeneratedKeyHolder();
        jdbc.update(con -> { PreparedStatement ps = con.prepareStatement("INSERT INTO customer_order(order_no,user_id,coupon_id,status,payment_status,shipment_status,subtotal,discount_amount,grand_total,shipping_address_snapshot) VALUES(?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, orderNo); ps.setLong(2, userId); ps.setObject(3, couponId); ps.setString(4,"CONFIRMED"); ps.setString(5,"PAID"); ps.setString(6,"PROCESSING"); ps.setBigDecimal(7, subtotal); ps.setBigDecimal(8, discount); ps.setBigDecimal(9,total); ps.setString(10,address); return ps; }, kh);
        Long orderId = kh.getKey().longValue();
        for (CartLine line: lines) {
            jdbc.update("INSERT INTO order_item(order_id,book_id,quantity,unit_price,final_line_total,title_snapshot) VALUES(?,?,?,?,?,?)", orderId, line.getBookId(), line.getQuantity(), line.getPrice(), line.getLineTotal(), line.getTitle());
            Integer balance = jdbc.queryForObject("SELECT stock_on_hand FROM book WHERE id=?", Integer.class, line.getBookId());
            Long itemId = jdbc.queryForObject("SELECT MAX(id) FROM order_item WHERE order_id=? AND book_id=?", Long.class, orderId, line.getBookId());
            jdbc.update("INSERT INTO inventory_transaction(book_id, order_item_id, tx_type, quantity_delta, balance_after, note) VALUES(?,?,?,?,?,?)", line.getBookId(), itemId, "SALE", -line.getQuantity(), balance, "Order " + orderNo);
        }
        jdbc.update("INSERT INTO payment(order_id,provider,method,provider_txn_id,amount,status) VALUES(?,?,?,?,?,?)", orderId, "DEMO", "COD/DEMO", "TXN-" + System.nanoTime(), total, "PAID");
        jdbc.update("INSERT INTO shipment(order_id,carrier,tracking_no,status,shipped_at) VALUES(?,?,?,?,?)", orderId, "FastShip", "TRK-" + System.nanoTime(), "CREATED", null);
        return orderId;
    }

    public List<OrderSummary> findOrders(Long userId, boolean all) {
        if (all) return jdbc.query("SELECT o.*, u.full_name customer_name FROM customer_order o JOIN app_user u ON u.id=o.user_id ORDER BY o.placed_at DESC", RowMappers.ORDER);
        return jdbc.query("SELECT o.*, u.full_name customer_name FROM customer_order o JOIN app_user u ON u.id=o.user_id WHERE o.user_id=? ORDER BY o.placed_at DESC", RowMappers.ORDER, userId);
    }

    public void updateStatus(Long id, String status, String paymentStatus, String shipmentStatus) {
        jdbc.update("UPDATE customer_order SET status=?, payment_status=?, shipment_status=?, updated_at=CURRENT_TIMESTAMP WHERE id=?", status, paymentStatus, shipmentStatus, id);
    }

    public List<java.util.Map<String,Object>> monthlyRevenue() {
        return jdbc.queryForList("SELECT FORMATDATETIME(placed_at, 'yyyy-MM') label, SUM(grand_total) value FROM customer_order GROUP BY FORMATDATETIME(placed_at, 'yyyy-MM') ORDER BY label");
    }
}
