package com.dreamtracker.app.user.domain.ports;

import com.dreamtracker.app.fixtures.UserFixtures;
import com.dreamtracker.app.infrastructure.auth.PasswordResetResponse;
import com.dreamtracker.app.infrastructure.auth.PasswordResetTokenGenerator;
import com.dreamtracker.app.infrastructure.mail.MailService;
import com.dreamtracker.app.user.adapters.api.EnterPasswordResetRequest;
import com.dreamtracker.app.user.adapters.api.PasswordResetRequest;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class DomainUserServiceTest implements UserFixtures {


    private final UserRepositoryPort userRepositoryPort = Mockito.mock(UserRepositoryPort.class);
    private final MailService mailService = Mockito.mock(MailService.class);
    private final CurrentUserProvider currentUserProvider = Mockito.mock(CurrentUserProvider.class);
    private final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    private final DomainUserService domainUserService = new DomainUserService(userRepositoryPort,currentUserProvider ,mailService,passwordEncoder);
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
        var actual = domainUserService.requestPasswordReset(request);
        // then
        assertThat(actual).isEqualTo(expected);
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
        var request = new PasswordResetRequest("Valid1@Password","Valid1@Password",user.getResetToken());
        logger.trace(user.toString());
        // when
        when(userRepositoryPort.getByResetToken(user.getResetToken())).thenReturn(user);
        when(passwordEncoder.encode("Valid1@Password")).thenReturn("changed");
        when(userRepositoryPort.save(user)).thenReturn(user);

        var actual = domainUserService.resetPassword(request);

        // tnen
        assertThat(actual).isEqualTo(true);
        assertThat(user.getPassword()).isEqualTo("changed");
        assertThat(user.getResetToken()).isNotEqualTo(currentResetToken);
    }
}