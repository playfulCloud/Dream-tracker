package com.dreamtracker.app.user.domain.ports;

import com.dreamtracker.app.fixtures.UserFixtures;
import com.dreamtracker.app.habit.domain.ports.HabitService;
import com.dreamtracker.app.infrastructure.auth.PasswordResetResponse;
import com.dreamtracker.app.infrastructure.auth.PasswordResetTokenGenerator;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.infrastructure.mail.MailService;
import com.dreamtracker.app.user.adapters.api.EnterPasswordResetRequest;
import com.dreamtracker.app.user.adapters.api.PasswordResetRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DomainUserServiceTest implements UserFixtures {


    private final UserRepositoryPort userRepositoryPort = Mockito.mock(UserRepositoryPort.class);
    private final HabitService habitService = Mockito.mock(HabitService.class);
    private final PositionService positionService = Mockito.mock(PositionService.class);
    private final MailService mailService = Mockito.mock(MailService.class);
    private final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    private final DomainUserService domainUserService = new DomainUserService(userRepositoryPort, habitService, positionService, mailService, passwordEncoder);
    private static final Logger logger = LoggerFactory.getLogger(DomainUserServiceTest.class);

    @Test
    void requestPasswordResetPositiveTestCase() {
        // given
        var request = new EnterPasswordResetRequest("sample@gmail.com");
        var expected = new PasswordResetResponse("Password reset email sent");
        var user = getUser().email("sample@gmail.com").uuid(
                        UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e")
                ).fullName("Jakub Testowski")
                .resetToken(PasswordResetTokenGenerator.generateResetToken("smaple@gmail.com"))
                .build();
        when(userRepositoryPort.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(mailService.sendPasswordResetMail(user.getEmail(), user.getResetToken(), user.getFullName())).thenReturn(true);
        // when
        var actual = domainUserService.requestPasswordReset(request);
        // then
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void requestPasswordResetUserNotFound() {
        // given
        var request = new EnterPasswordResetRequest("sample@gmail.com");
        var expected = new PasswordResetResponse("There is no user with that email address");
        var user = getUser().email("sample@gmail.com").uuid(
                        UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e")
                ).fullName("Jakub Testowski")
                .resetToken(PasswordResetTokenGenerator.generateResetToken("smaple@gmail.com"))
                .build();
        when(userRepositoryPort.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(mailService.sendPasswordResetMail(user.getEmail(), user.getResetToken(), user.getFullName())).thenReturn(false);
        // when
        assertThatThrownBy(() -> {
            domainUserService.requestPasswordReset(request);
        }) // then
                .isInstanceOf(EntityNotFoundException.class).hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
    }

    @Test
    void resetPasswordPositiveTestCase() {
        // given
        var currentResetToken = PasswordResetTokenGenerator.generateResetToken("smaple@gmail.com");
        var user = getUser().email("sample@gmail.com").uuid(
                        UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e")
                ).fullName("Jakub Testowski")
                .resetToken(currentResetToken)
                .password("previousPassword")
                .build();
        var request = new PasswordResetRequest("Valid1@Password", "Valid1@Password", user.getResetToken());
        logger.trace(user.toString());
        // when
        when(userRepositoryPort.getByResetToken(user.getResetToken())).thenReturn(user);
        when(passwordEncoder.encode("Valid1@Password")).thenReturn("changed");
        when(userRepositoryPort.save(user)).thenReturn(user);

        domainUserService.resetPassword(request);

        // then
        assertThat(user.getPassword()).isEqualTo("changed");
        assertThat(user.getResetToken()).isNotEqualTo(currentResetToken);
    }


    @Test
    void confirmAccountPositiveTestCase() {
        var user = getUser().email("sample@gmail.com").uuid(
                        UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e")
                ).fullName("Jakub Testowski")
                .password("previousPassword")
                .build();
        when(userRepositoryPort.getById(user.getUuid())).thenReturn(user);
        when(userRepositoryPort.save(user)).thenReturn(user);

        var userResponse = domainUserService.confirmAccount(user.getUuid());

        assertThat(userResponse.confirmed()).isEqualTo(true);
    }



   @Test
   void removeUnconfirmedUsersPositiveTestCase(){
        // given
        var yesterday = Instant.now().minus(2,ChronoUnit.DAYS);
       Date date = Date.from(yesterday);
        var user = getUser().email("sample@gmail.com").uuid(
                       UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e")
               ).fullName("Jakub Testowski")
               .password("previousPassword")
                .createdAt(date)
               .build();
        when(userRepositoryPort.findByConfirmedFalse()).thenReturn(List.of(user));
        // when
        domainUserService.removeUnconfirmedUsers(LocalDate.now());
        // then
        verify(userRepositoryPort).deleteByUuid(user.getUuid());
       verify(habitService).deleteUser(user.getUuid());
       verify(positionService).deleteUser(user.getUuid());
   }

    @Test
    void removeUnconfirmedUsersPositiveTestCaseZeroUsersDeleted(){
        // given
        var yesterday = Instant.now().minus(1,ChronoUnit.DAYS);
        Date date = Date.from(yesterday);
        var user = getUser().email("sample@gmail.com").uuid(
                        UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e")
                ).fullName("Jakub Testowski")
                .password("previousPassword")
                .createdAt(date)
                .build();
        when(userRepositoryPort.findByConfirmedFalse()).thenReturn(List.of(user));
        // when
        domainUserService.removeUnconfirmedUsers(LocalDate.now());
        // then
        verify(userRepositoryPort, never()).deleteByUuid(user.getUuid());
        verify(habitService,never()).deleteUser(user.getUuid());
        verify(positionService,never()).deleteUser(user.getUuid());
    }


    @Test
    void removeUnconfirmedUsersPositiveTestCase1UserDeleted(){
        // given
        var yesterday = Instant.now().minus(1,ChronoUnit.DAYS);
        Date date = Date.from(yesterday);
        var preyesterday = Instant.now().minus(2,ChronoUnit.DAYS);
        Date user2Date = Date.from(preyesterday);

        var user = getUser().email("sample@gmail.com").uuid(
                        UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e")
                ).fullName("Jakub Testowski")
                .password("previousPassword")
                .createdAt(date)
                .build();
        var user2 = getUser().email("sample@gmail.com").uuid(
                        UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e")
                ).fullName("Jakub Testowski")
                .password("previousPassword")
                .createdAt(user2Date)
                .build();
        when(userRepositoryPort.findByConfirmedFalse()).thenReturn(List.of(user,user2));
        // when
        domainUserService.removeUnconfirmedUsers(LocalDate.now());
        // then
        verify(userRepositoryPort, times(1)).deleteByUuid(user.getUuid());
        verify(habitService,times(1)).deleteUser(user.getUuid());
        verify(positionService,times(1)).deleteUser(user.getUuid());
    }

}