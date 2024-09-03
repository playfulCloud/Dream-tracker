package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.user.domain.model.User;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface SpringDataUserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    User getByResetToken(String resetToken);
    User getById(UUID uuid);
    @Query("SELECT u FROM User u WHERE u.confirmed = false AND u.createdAt <= :date")
    List<User> findUnconfirmedUsersCreatedBefore(@Param("date") LocalDate date);
}
