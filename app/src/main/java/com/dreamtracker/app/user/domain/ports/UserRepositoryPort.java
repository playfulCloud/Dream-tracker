package com.dreamtracker.app.user.domain.ports;

import com.dreamtracker.app.user.domain.model.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
}
