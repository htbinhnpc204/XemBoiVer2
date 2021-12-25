package com.htbinh.xemboiver2.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class Account {
    private final String user;
    private final String password;

    public Account(String userId, String password) {
        this.user = userId;
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}