package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.user.domain.model.User;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    User getByResetToken(String resetToken);
}
