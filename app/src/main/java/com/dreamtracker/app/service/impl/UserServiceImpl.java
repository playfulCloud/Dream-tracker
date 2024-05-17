package com.dreamtracker.app.service.impl;

import com.dreamtracker.app.domain.entity.User;
import com.dreamtracker.app.domain.repository.UserRepository;
import com.dreamtracker.app.domain.response.UserResponse;
import com.dreamtracker.app.infrastructure.security.CurrentUserProvider;
import com.dreamtracker.app.service.UserService;

import java.util.Optional;
import java.util.UUID;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final CurrentUserProvider currentUserProvider;

  @Override
  public User save(User user) {
    return userRepository.save(user);
  }

  @Override
  public UserResponse createSampleUser() {
    var sampleUser =
        User.builder()
            .uuid(currentUserProvider.getCurrentUser())
            .name("John")
            .surname("Doe")
            .build();

    var userSavedToDB = save(sampleUser);

    return UserResponse.builder()
        .uuid(userSavedToDB.getUuid())
        .name(userSavedToDB.getName())
        .surname(userSavedToDB.getSurname())
        .build();
  }

  @Override
  public Optional<User> findById(UUID uuid) {
    return userRepository.findById(uuid);
  }
}
