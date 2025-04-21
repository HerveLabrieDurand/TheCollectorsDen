package com.collectorsden.demo.auth.dto.response;

public enum UserRole {
    ADMIN,
    USER;

    public static UserRole fromUserRole(com.collectorsden.demo.model.enums.user.UserRole userRole) {
        switch (userRole) {
            case ADMIN:
                return ADMIN;
            case USER:
                return USER;
            default:
                throw new IllegalArgumentException("Invalid user role: " + userRole);
        }
    }
}
