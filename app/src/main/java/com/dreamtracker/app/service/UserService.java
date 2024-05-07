package com.dreamtracker.app.service;

import com.dreamtracker.app.entity.User;
import com.dreamtracker.app.response.UserResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User save(User user);
    UserResponse createSampleUser();
    Optional<User>findById(UUID uuid);
}
