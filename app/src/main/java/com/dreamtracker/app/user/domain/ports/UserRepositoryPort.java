package com.dreamtracker.app.user.domain.ports;

import com.dreamtracker.app.user.domain.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    User getByResetToken(String resetToken);
    User getById(UUID uuid);
    List<User> findUnconfirmedUsersCreatedBefore();
}
