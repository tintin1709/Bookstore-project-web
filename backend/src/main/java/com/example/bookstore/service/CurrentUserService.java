package com.example.bookstore.service;

import com.example.bookstore.model.AppUser;
import com.example.bookstore.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    private final UserRepository users;
    public CurrentUserService(UserRepository users) { this.users = users; }
    public AppUser current(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) return null;
        return users.findByEmail(authentication.getName()).orElse(null);
    }
}
