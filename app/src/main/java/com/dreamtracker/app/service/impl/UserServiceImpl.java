package com.dreamtracker.app.service.impl;

import com.dreamtracker.app.exception.EntitySaveException;
import com.dreamtracker.app.exception.ExceptionMessages;
import com.dreamtracker.app.response.UserResponse;
import com.dreamtracker.app.security.CurrentUserProvider;
import com.dreamtracker.app.entity.User;
import com.dreamtracker.app.entity.Goal;
import com.dreamtracker.app.entity.Habit;
import com.dreamtracker.app.repository.UserRepository;
import com.dreamtracker.app.service.UserService;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
            .goals(new ArrayList<Goal>())
            .habits(new ArrayList<Habit>())
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
