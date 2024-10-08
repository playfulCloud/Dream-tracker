package com.dreamtracker.app.user.domain.ports;

import com.dreamtracker.app.habit.domain.ports.HabitService;
import com.dreamtracker.app.infrastructure.auth.PasswordResetResponse;
import com.dreamtracker.app.infrastructure.auth.PasswordResetTokenGenerator;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.infrastructure.mail.MailService;
import com.dreamtracker.app.user.adapters.api.EnterPasswordResetRequest;
import com.dreamtracker.app.user.adapters.api.PasswordResetRequest;
import com.dreamtracker.app.user.adapters.api.UserResponse;
import com.dreamtracker.app.user.domain.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
public class DomainUserService implements UserService {

    private final UserRepositoryPort userRepositoryPort;
    private final HabitService habitService;
    private final PositionService positionService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(DomainUserService.class);


    @Override
    public Optional<User> findById(UUID uuid) {
        return userRepositoryPort.findById(uuid);
    }

    @Override
    public PasswordResetResponse requestPasswordReset(EnterPasswordResetRequest resetRequest) {
        var user = userRepositoryPort.findByEmail(resetRequest.email()).orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
        mailService.sendPasswordResetMail(user.getEmail(), user.getResetToken(), user.getFullName());
        return new PasswordResetResponse("Password reset email sent");
    }

    @Override
    public UserResponse confirmAccount(UUID userUUID) {
        var user = userRepositoryPort.getById(userUUID);
        user.setConfirmed(true);
        var userSavedToDB = userRepositoryPort.save(user);
        return mapToUserResponse(userSavedToDB);
    }

    @Override
    public void resetPassword(PasswordResetRequest resetRequest) {
        var user = userRepositoryPort.getByResetToken(resetRequest.resetToken());
        user.setPassword(passwordEncoder.encode(resetRequest.password()));
        user.setResetToken(PasswordResetTokenGenerator.generateResetToken(user.getEmail()));
        userRepositoryPort.save(user);
    }

    @Override
    public void removeUnconfirmedUsers(LocalDate date) {
        LocalDate dateBefore = date.minusDays(1);
        var unconfirmedUsers = userRepositoryPort.findByConfirmedFalse();
        unconfirmedUsers.stream()
                .filter(user -> isDifferentDay(user.getCreatedAt(), dateBefore))
                .forEach(user -> deleteById(user.getUuid()));

    }

    private boolean isDifferentDay(Date date, LocalDate localDate) {
        LocalDate dateToLocalDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        return !dateToLocalDate.equals(localDate);
    }

    @Override
    @Transactional
    public void deleteById(UUID uuid) {
        habitService.deleteUser(uuid);
        positionService.deleteUser(uuid);
        userRepositoryPort.deleteByUuid(uuid);
    }


    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder().uuid(user.getUuid()).email(user.getEmail()).confirmed(user.isConfirmed()).build();
    }


}
