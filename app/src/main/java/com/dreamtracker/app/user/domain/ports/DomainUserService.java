package com.dreamtracker.app.user.domain.ports;

import com.dreamtracker.app.infrastructure.auth.PasswordResetResponse;
import com.dreamtracker.app.infrastructure.auth.PasswordResetTokenGenerator;
import com.dreamtracker.app.infrastructure.mail.MailService;
import com.dreamtracker.app.user.adapters.api.EnterPasswordResetRequest;
import com.dreamtracker.app.user.adapters.api.PasswordResetRequest;
import com.dreamtracker.app.user.adapters.api.UserResponse;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.domain.model.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

@Data
public class DomainUserService implements UserService {

    private final UserRepositoryPort userRepositoryPort;
    private final CurrentUserProvider currentUserProvider;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    public DomainUserService(UserRepositoryPort userRepositoryPort, CurrentUserProvider currentUserProvider, MailService mailService, PasswordEncoder passwordEncoder) {
        this.userRepositoryPort = userRepositoryPort;
        this.currentUserProvider = currentUserProvider;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserResponse createSampleUser() {
        var sampleUser =
                User.builder()
                        .uuid(currentUserProvider.getCurrentUser())
                        .fullName("John Doe")
                        .email("john.doe@example.com")
                        .password("securepassword")
                        .build();

        var userSavedToDB = userRepositoryPort.save(sampleUser);

        return UserResponse.builder()
                .uuid(userSavedToDB.getUuid())
                .fullName(userSavedToDB.getFullName())
                .email(userSavedToDB.getEmail())
                .build();
    }


    @Override
    public Optional<User> findById(UUID uuid) {
        return userRepositoryPort.findById(uuid);
    }

    @Override
    public PasswordResetResponse requestPasswordReset(EnterPasswordResetRequest resetRequest) {
        var user = userRepositoryPort.findByEmail(resetRequest.email());
        if (user.isPresent()) {
            var foundUser = user.get();
            mailService.sendPasswordResetMail(foundUser.getEmail(),user.get().getResetToken(), foundUser.getFullName());
            return new PasswordResetResponse("Password reset email sent");
        }
        return new PasswordResetResponse("There is no user with that email address");
    }

    @Override
    public Boolean resetPassword(PasswordResetRequest resetRequest) {
        var user = userRepositoryPort.getByResetToken(resetRequest.resetToken());
        user.setPassword(passwordEncoder.encode(resetRequest.password()));
        user.setResetToken(PasswordResetTokenGenerator.generateResetToken(user.getEmail()));
        userRepositoryPort.save(user);
        return true;
    }



}
