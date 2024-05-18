package com.dreamtracker.app.user.domain.ports;

import com.dreamtracker.app.user.domain.model.User;
import com.dreamtracker.app.user.adapters.api.UserResponse;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User save(User user);
    UserResponse createSampleUser();
    Optional<User>findById(UUID uuid);
}
