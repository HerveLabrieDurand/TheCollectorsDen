package com.collectorsden.demo.repository;

import com.collectorsden.demo.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Integer> {
    Optional<ConfirmationToken> findByToken(String token);
}
