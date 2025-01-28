package com.collectorsden.demo.auth.controller;

import com.collectorsden.demo.auth.dto.request.AuthenticateRequest;
import com.collectorsden.demo.auth.dto.request.RegisterRequest;
import com.collectorsden.demo.auth.dto.response.AuthenticationResponse;
import com.collectorsden.demo.auth.service.AuthenticationService;
import com.collectorsden.demo.auth.service.EmailService;
import com.collectorsden.demo.exception.GlobalExceptionHandler;
import com.collectorsden.demo.exception.auth.InvalidCredentialsException;
import com.collectorsden.demo.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void Authenticate_Should_Return_AuthenticationResponse_When_Valid_Request() throws Exception {
        // Arrange
        AuthenticateRequest request = new AuthenticateRequest("user@example.com", "password123");
        AuthenticationResponse response = new AuthenticationResponse("mockJwtToken");

        when(authenticationService.authenticate(any(AuthenticateRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("mockJwtToken"));

        verify(authenticationService).authenticate(any(AuthenticateRequest.class));
    }

    @Test
    void Register_Should_Return_Void_When_Valid_Request() throws Exception {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
                .email("user@example.com")
                .password("password123")
                .name("User")
                .build();

        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(any(User.class));

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(authenticationService).register(any(RegisterRequest.class));
    }
}
