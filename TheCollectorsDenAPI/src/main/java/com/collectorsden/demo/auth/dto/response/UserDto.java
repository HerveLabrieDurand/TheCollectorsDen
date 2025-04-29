package com.collectorsden.demo.auth.dto.response;

import com.collectorsden.demo.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    public Integer userId;
    public String name;
    public String email;
    public String country;
    public String address;
    public String city;
    public String postalCode;
    public String profilePictureUrl;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public UserStatus status;
    public String phoneNumber;
    public String preferences; // stored as json
    public LocalDateTime lastLogin;
    public UserRole role;

    public static UserDto fromUser(User user) {
        return UserDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .country(user.getCountry())
                .address(user.getAddress())
                .city(user.getCity())
                .postalCode(user.getPostalCode())
                .profilePictureUrl(user.getProfilePictureUrl())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .status(com.collectorsden.demo.auth.dto.response.UserStatus.fromUserStatus(user.getStatus()))
                .phoneNumber(user.getPhoneNumber())
                .preferences(user.getPreferences())
                .lastLogin(user.getLastLogin())
                .role(com.collectorsden.demo.auth.dto.response.UserRole.fromUserRole(user.getRole()))
                .build();
    }
}
