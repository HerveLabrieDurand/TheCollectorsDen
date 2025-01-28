package com.collectorsden.demo.repository;

import com.collectorsden.demo.model.ConfirmationToken;
import com.collectorsden.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Integer> {
    Optional<ConfirmationToken> findByToken(String token);

    @Transactional
    void deleteAllByUser(User user);
}
