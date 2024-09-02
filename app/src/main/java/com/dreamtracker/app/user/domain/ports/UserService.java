package com.dreamtracker.app.user.domain.ports;

import com.dreamtracker.app.infrastructure.auth.PasswordResetResponse;
import com.dreamtracker.app.user.adapters.api.EnterPasswordResetRequest;
import com.dreamtracker.app.user.adapters.api.PasswordResetRequest;
import com.dreamtracker.app.user.adapters.api.UserResponse;
import com.dreamtracker.app.user.domain.model.User;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserResponse createSampleUser();
    Optional<User>findById(UUID uuid);
    PasswordResetResponse requestPasswordReset(EnterPasswordResetRequest resetRequest);
    void resetPassword(PasswordResetRequest resetRequest);
}
