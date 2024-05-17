package com.dreamtracker.app.service;

import com.dreamtracker.app.domain.entity.User;
import com.dreamtracker.app.domain.response.UserResponse;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User save(User user);
    UserResponse createSampleUser();
    Optional<User>findById(UUID uuid);
}
