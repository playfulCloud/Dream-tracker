package com.dreamtracker.app.user.domain.ports;

import com.dreamtracker.app.infrastructure.auth.PasswordResetResponse;
import com.dreamtracker.app.user.adapters.api.EnterPasswordResetRequest;
import com.dreamtracker.app.user.adapters.api.PasswordResetRequest;
import com.dreamtracker.app.user.adapters.api.UserResponse;
import com.dreamtracker.app.user.domain.model.User;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<User>findById(UUID uuid);
    PasswordResetResponse requestPasswordReset(EnterPasswordResetRequest resetRequest);
    UserResponse confirmAccount(UUID userUUID);
    void resetPassword(PasswordResetRequest resetRequest);
    void removeUnconfirmedUsers(LocalDate date);
    void deleteById(UUID uuid);
}
