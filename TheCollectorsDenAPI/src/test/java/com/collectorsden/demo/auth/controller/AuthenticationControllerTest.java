package com.collectorsden.demo.auth.controller;

import com.collectorsden.demo.auth.dto.request.AuthenticateRequest;
import com.collectorsden.demo.auth.dto.request.RegisterRequest;
import com.collectorsden.demo.auth.dto.response.AuthenticationResponse;
import com.collectorsden.demo.auth.service.AuthenticationService;
import com.collectorsden.demo.auth.service.EmailService;
import com.collectorsden.demo.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
        this.mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void Authenticate_Should_Return_AuthenticationResponse_When_Valid_Request() throws Exception {
        // Arrange
        AuthenticateRequest request = new AuthenticateRequest("user@example.com", "password123");
        AuthenticationResponse response = new AuthenticationResponse("mockJwtToken");

        when(this.authenticationService.authenticate(any(AuthenticateRequest.class))).thenReturn(response);

        // Act & Assert
        this.mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("mockJwtToken"));

        verify(this.authenticationService).authenticate(any(AuthenticateRequest.class));
    }

    @Test
    void Authenticate_Should_Return_Failure_When_Invalid_Request() throws Exception {
        // Arrange
        AuthenticateRequest request = new AuthenticateRequest("notAnEmail", "");
        AuthenticationResponse response = new AuthenticationResponse("mockJwtToken");

        // Act & Assert
        this.mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void Register_Should_Return_Success_When_Valid_Request() throws Exception {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
                .email("user@example.com")
                .password("password123")
                .name("User")
                .build();

        when(this.authenticationService.register(any(RegisterRequest.class))).thenReturn(any(User.class));

        // Act & Assert
        this.mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(this.authenticationService).register(any(RegisterRequest.class));
    }

    @Test
    void Register_Should_Return_Failure_When_Invalid_Request() throws Exception {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
                .email("notAnEmail")
                .password("")
                .name("")
                .build();

        // Act & Assert
        this.mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ConfirmEmail_Should_Succeed_When_Valid_Request() throws Exception {
        // Arrange
        String token = "mockJwtToken";

        // Act & Assert
        this.mockMvc.perform(post("/api/v1/auth/confirm-email")
                        .param("token", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(this.emailService).confirmEmail(token);
    }

    @Test
    void ConfirmEmail_Should_Fail_When_Invalid_Token() throws Exception {
        // Arrange
        String token = "";

        // Act & Assert
        this.mockMvc.perform(post("/api/v1/auth/confirm-email")
                        .param("token", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ResendConfirmEmail_Should_Succeed_When_Valid_Request() throws Exception {
        // Arrange
        String email = "example@thecollectorsden.com";

        // Act & Assert
        this.mockMvc.perform(post("/api/v1/auth/resend-confirmation-email")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(this.emailService).resendConfirmationEmail(email);
    }

    @Test
    void ResendConfirmEmail_Should_Fail_When_Invalid_Email() throws Exception {
        // Arrange
        String email = "notAnEmail";

        // Act & Assert
        this.mockMvc.perform(post("/api/v1/auth/resend-confirmation-email")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
