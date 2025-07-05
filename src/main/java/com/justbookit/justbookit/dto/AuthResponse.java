package com.justbookit.justbookit.dto;

import com.justbookit.justbookit.model.User;

public class AuthResponse {
    private String token;
    private User user;


    public AuthResponse(String token, User user) {
        this.token = token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }
}

