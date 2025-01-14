package com.collectorsden.demo.config.security;

import com.collectorsden.demo.config.security.constants.SecurityConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    // Spring Security 6+ uses a Lambda DSL, rest is deprecated
    // See: https://docs.spring.io/spring-security/reference/migration-7/configuration.html#_use_the_lambda_dsl
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(SecurityConstants.WHITE_LIST_URLS).permitAll() // Allow public access to whitelist URLs
                        .anyRequest().authenticated() // Require authentication for all other requests
                )
//                .formLogin(formLogin -> formLogin
//                        .loginPage("/login") // Specify a custom login page
//                        .permitAll() // Allow everyone to access the login page
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout") // Specify a custom logout URL
//                        .logoutSuccessUrl("/welcome") // Redirect after logout
//                        .permitAll()
//                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Enable "remember me" functionality

        return http.build();
    }
}
