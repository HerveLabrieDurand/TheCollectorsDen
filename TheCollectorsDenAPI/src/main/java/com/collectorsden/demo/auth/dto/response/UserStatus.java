package com.collectorsden.demo.auth.dto.response;

public enum UserStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED;

    public static UserStatus fromUserStatus(com.collectorsden.demo.model.enums.user.UserStatus userStatus) {
        switch (userStatus) {
            case ACTIVE:
                return ACTIVE;
            case INACTIVE:
                return INACTIVE;
            case SUSPENDED:
                return SUSPENDED;
            default:
                throw new IllegalArgumentException("Invalid user status: " + userStatus);
        }
    }
}
