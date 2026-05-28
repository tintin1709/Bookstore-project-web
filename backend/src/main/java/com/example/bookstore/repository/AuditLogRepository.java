package com.example.bookstore.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AuditLogRepository {
    private final JdbcTemplate jdbc;
    public AuditLogRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public void log(Long actorId, String entity, Long entityId, String action, String oldData, String newData) {
        jdbc.update("INSERT INTO audit_log(actor_user_id,entity_type,entity_id,action,old_data,new_data) VALUES(?,?,?,?,?,?)", actorId, entity, entityId, action, oldData, newData);
    }
    public List<Map<String,Object>> latest() {
        return jdbc.queryForList("SELECT a.*, COALESCE(u.full_name,'System') actor FROM audit_log a LEFT JOIN app_user u ON u.id=a.actor_user_id ORDER BY a.occurred_at DESC LIMIT 50");
    }
}
