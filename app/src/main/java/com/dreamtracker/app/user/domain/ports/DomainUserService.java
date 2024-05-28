package com.dreamtracker.app.user.domain.ports;

import com.dreamtracker.app.user.adapters.api.UserResponse;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.domain.model.User;
import java.util.Optional;
import java.util.UUID;
import lombok.Data;

@Data
public class DomainUserService implements UserService {

  private final UserRepositoryPort userRepositoryPort;
  private final CurrentUserProvider currentUserProvider;



  @Override
  public UserResponse createSampleUser() {
    var sampleUser =
        User.builder()
            .uuid(currentUserProvider.getCurrentUser())
            .name("John")
            .surname("Doe")
            .build();

    var userSavedToDB = userRepositoryPort.save(sampleUser);

    return UserResponse.builder()
        .uuid(userSavedToDB.getUuid())
        .name(userSavedToDB.getName())
        .surname(userSavedToDB.getSurname())
        .build();
  }

  @Override
  public Optional<User> findById(UUID uuid) {
    return userRepositoryPort.findById(uuid);
  }
}
