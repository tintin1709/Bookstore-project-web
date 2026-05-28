package com.example.bookstore.repository;

import com.example.bookstore.model.Notification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationRepository {
    private final JdbcTemplate jdbc;

    public NotificationRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Notification> findByUser(Long userId) {
        return jdbc.query( "SELECT * FROM notification WHERE user_id=? ORDER BY sent_at DESC", 
                            RowMappers.NOTIFICATION, userId);
    }

    public int unreadCount(Long userId) {
        return jdbc.queryForObject("SELECT COUNT(*) FROM notification WHERE user_id=? AND status='UNREAD'",
                                    Integer.class, userId);
    }

    public void markRead(Long id, Long userId) {
        jdbc.update("UPDATE notification SET status='READ', read_at=CURRENT_TIMESTAMP WHERE id=? AND user_id=?",
                    id, userId);
    }
}
