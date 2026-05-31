package com.repository;

import com.model.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReservationRepository {
    private final JdbcTemplate jdbc;

    public ReservationRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void createReservation(Long userId, Long bookId, int quantity) {
    String bookTitle = jdbc.queryForObject(
            "SELECT title FROM book WHERE id = ?",
            String.class,
            bookId
    );

    jdbc.update(
            "INSERT INTO reservation(user_id, book_id, quantity, status, expires_at) " +
            "VALUES (?, ?, ?, ?, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 7 DAY))",
            userId, bookId, quantity, "WAITING"
    );

    Long resId = jdbc.queryForObject(
            "SELECT MAX(id) FROM reservation WHERE user_id = ?",
            Long.class,
            userId
    );

    jdbc.update(
            "INSERT INTO notification(user_id, reservation_id, notification_type, title, message) " +
            "VALUES (?, ?, ?, ?, ?)",
            userId,
            resId,
            "RESERVATION",
            "Reservation request submitted successfully",
            "Your reservation request for \"" + bookTitle + "\" has been received. " +
            "Quantity: " + quantity + ". Please wait for staff approval."
    );
}

    public List<Reservation> findByUser(Long userId) {
        return jdbc.query(
                "SELECT r.*, u.full_name customer_name, b.title book_title FROM reservation r JOIN app_user u ON u.id=r.user_id JOIN book b ON b.id=r.book_id WHERE r.user_id=? ORDER BY r.reserved_at DESC",
                RowMappers.RESERVATION, userId);
    }

    public List<Reservation> findAll() {
        return jdbc.query(
                "SELECT r.*, u.full_name customer_name, b.title book_title FROM reservation r JOIN app_user u ON u.id=r.user_id JOIN book b ON b.id=r.book_id ORDER BY r.reserved_at DESC",
                RowMappers.RESERVATION);
    }

    public void updateStatus(Long id, String status) {
        jdbc.update("UPDATE reservation SET status=?, notified_at=CURRENT_TIMESTAMP WHERE id=?", status, id);
        Long userId = jdbc.queryForObject("SELECT user_id FROM reservation WHERE id=?", Long.class, id);
        jdbc.update(
                "INSERT INTO notification(user_id,reservation_id,notification_type,title,message) VALUES(?,?,?,?,?)",
                userId, id, "RESERVATION_STATUS", "Reservation " + status,
                "Your reservation status has been updated to " + status + ".");
    }
}
