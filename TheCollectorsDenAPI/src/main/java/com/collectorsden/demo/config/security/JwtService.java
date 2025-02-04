package com.collectorsden.demo.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {

    private String jwtSecret;
    private long jwtExpiration;

    public JwtService(
            @Value("${jwt.secret:}") String jwtSecretProp,  // Read from properties first
            @Value("${jwt.expiration:0}") long jwtExpirationProp  // Read from properties first
    ) {
        // Use environment variables if properties are empty
        this.jwtSecret = !jwtSecretProp.isEmpty() ? jwtSecretProp : System.getenv("JWT_SECRET");
        String expirationStr = System.getenv("JWT_EXPIRATION");

        if (this.jwtSecret == null || this.jwtSecret.isEmpty()) {
            throw new IllegalStateException("JWT Secret is not set. Check environment variables or application properties.");
        }

        this.jwtExpiration = jwtExpirationProp != 0 ? jwtExpirationProp : (expirationStr != null ? Long.parseLong(expirationStr) : 0);

        if (this.jwtExpiration == 0) {
            throw new IllegalStateException("JWT Expiration is not set. Check environment variables or application properties.");
        }
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());
        return generateToken(claims, userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + this.jwtExpiration))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        final String username = extractUsername(jwtToken);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(jwtToken);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String jwtToken) {
        return Jwts.parser()
                .setSigningKey(getSigningKey()) // Set the signing key for validation
                .build()
                .parseClaimsJws(jwtToken)
                .getBody(); // Extract claims
    }

    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes); // Create HMAC-SHA256 key
    }
}
