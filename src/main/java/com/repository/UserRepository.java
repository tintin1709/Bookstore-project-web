package com.repository;

import com.model.AppUser;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public UserDetails loadUserDetailsByEmail(String email) {
        AppUser user = findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!"ACTIVE".equals(user.getStatus()))
            throw new UsernameNotFoundException("User is inactive");
        List<SimpleGrantedAuthority> authorities = rolesOf(user.getId()).stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();
        return new User(user.getEmail(), user.getPasswordHash(), user.isEmailVerified(), true, true, true, authorities);
    }

    public Optional<AppUser> findByEmail(String email) {
        try {
            AppUser user = jdbc.queryForObject("SELECT * FROM app_user WHERE email=?", RowMappers.USER, email);
            user.setRoles(rolesOf(user.getId()));
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<AppUser> findById(Long id) {
        try {
            AppUser user = jdbc.queryForObject("SELECT * FROM app_user WHERE id=?", RowMappers.USER, id);
            user.setRoles(rolesOf(user.getId()));
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<AppUser> findAll(String keyword) {
        String like = "%" + (keyword == null ? "" : keyword.toLowerCase()) + "%";
        List<AppUser> users = jdbc.query(
                "SELECT * FROM app_user WHERE LOWER(email) LIKE ? OR LOWER(full_name) LIKE ? ORDER BY created_at DESC",
                RowMappers.USER, like, like);
        users.forEach(u -> u.setRoles(rolesOf(u.getId())));
        return users;
    }

    public Long createUser(String email, String hash, String fullName, String phone, String roleCode) {
        var keyHolder = new org.springframework.jdbc.support.GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO app_user(email,password_hash,full_name,phone,status,email_verified) VALUES(?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, email);
            ps.setString(2, hash);
            ps.setString(3, fullName);
            ps.setString(4, phone);
            ps.setString(5, "ACTIVE");
            ps.setBoolean(6, true);
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();
        assignRole(id, roleCode);
        return id;
    }

    public void updateProfile(Long id, String fullName, String phone) {
        jdbc.update("UPDATE app_user SET full_name=?, phone=?, updated_at=CURRENT_TIMESTAMP WHERE id=?", fullName,
                phone, id);
    }

    public void changePassword(Long id, String hash) {
        jdbc.update("UPDATE app_user SET password_hash=?, updated_at=CURRENT_TIMESTAMP WHERE id=?", hash, id);
    }

    public void updateStatusAndRole(Long id, String status, String roleCode) {
        jdbc.update("UPDATE app_user SET status=?, updated_at=CURRENT_TIMESTAMP WHERE id=?", status, id);
        jdbc.update("DELETE FROM user_role WHERE user_id=?", id);
        assignRole(id, roleCode);
    }

    public void assignRole(Long userId, String roleCode) {
        Long roleId = jdbc.queryForObject("SELECT id FROM role WHERE code=?", Long.class, roleCode);
        jdbc.update("INSERT INTO user_role(user_id, role_id) VALUES(?,?)", userId, roleId);
    }

    public List<String> rolesOf(Long userId) {
        return jdbc.queryForList("SELECT r.code FROM role r JOIN user_role ur ON r.id=ur.role_id WHERE ur.user_id=?",
                String.class, userId);
    }
}
