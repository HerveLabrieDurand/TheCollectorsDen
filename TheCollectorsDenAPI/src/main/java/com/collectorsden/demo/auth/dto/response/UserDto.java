package com.collectorsden.demo.auth.dto.response;

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
}
