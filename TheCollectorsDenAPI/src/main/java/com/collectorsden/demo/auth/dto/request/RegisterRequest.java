package com.collectorsden.demo.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Name must not be empty")

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Invalid email format")private String name;
    private String email;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Country must not be empty")
    private String country;

    @NotBlank(message = "Address must not be empty")
    private String address;

    @NotBlank(message = "City must not be empty")
    private String city;

    @NotBlank(message = "Postal Code must not be empty")
    private String postalCode;

    @NotBlank(message = "Phone Number must not be empty")
    private String phoneNumber;
}
