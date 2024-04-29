package com.dreamtracker.app.service;

import com.dreamtracker.app.entity.User;
import com.dreamtracker.app.response.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> save(User user);
    UserResponse createSampleUser();
}
