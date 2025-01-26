package com.collectorsden.demo.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String country;
    private String address;
    private String city;
    private String postalCode;
    private String phoneNumber;
}
