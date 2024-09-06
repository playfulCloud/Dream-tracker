package com.dreamtracker.app.user.adapters.api;


import com.dreamtracker.app.configuration.TestPostgresConfiguration;
import com.dreamtracker.app.fixtures.UserFixtures;
import com.dreamtracker.app.infrastructure.auth.AuthenticationResponse;
import com.dreamtracker.app.infrastructure.auth.LoginRequest;
import com.dreamtracker.app.infrastructure.auth.PasswordResetResponse;
import com.dreamtracker.app.infrastructure.auth.RegistrationRequest;
import com.dreamtracker.app.infrastructure.utils.DateService;
import com.dreamtracker.app.user.domain.ports.UserService;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Testcontainers
@ContextConfiguration(classes = TestPostgresConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest implements UserFixtures {

    @Autowired
    PostgreSQLContainer<?> postgreSQLContainer;
    @Autowired
    UserService userService;
    private final String BASE_URL = "/v1";
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private DateService dateService;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost");
        mailSender.setPort(3026);
        mailSender.setUsername("");
        mailSender.setPassword("");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.debug", "true");

        return mailSender;
    }


    private GreenMail greenMail;

    @BeforeEach
    void setUp() {
        greenMail = new GreenMail(new ServerSetup(3026, null, "smtp"));
        greenMail.start();
        logger.info("GreenMail started on port: " + greenMail.getSmtp().getPort());
        resetDatabase();
    }

    @AfterEach
    public void tearDown() {
        greenMail.stop();
    }

    private void resetDatabase() {
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE Users RESTART IDENTITY CASCADE");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @SneakyThrows
    @Test
    void registerPositiveTestCase() {
        // given
        var registrationRequest =
                new RegistrationRequest("john.doe@example.com", "Valid1@Password", "john.doe@example.com");
        // when
        var registrationResponse =
                restTemplate.postForEntity(
                        BASE_URL + "/auth/register", registrationRequest, UserResponse.class);
        // then
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertThat(receivedMessages[0].getSubject().toString().equals("Confirm Registration")).isEqualTo(true);
        assertThat(registrationResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(registrationResponse.getBody().email()).isEqualTo("john.doe@example.com");
    }


    @Test
    void registerCredentialsValidationExceptionThrown() {
        // given
        var registrationRequest =
                new RegistrationRequest("john.doe@example.com", "ValidPassword", "john.doe@example.com");
        // when
        var registrationResponse =
                restTemplate.postForEntity(
                        BASE_URL + "/auth/register", registrationRequest, UserResponse.class);
        // then
        assertThat(registrationResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void loginPositiveTestCase() {
        // given
        var registrationRequest =
                new RegistrationRequest("john.doe@example.com", "Valid1@Password", "john.doe@example.com");
        restTemplate.postForEntity(
                BASE_URL + "/auth/register", registrationRequest, UserResponse.class);
        var loginRequest = new LoginRequest("john.doe@example.com", "Valid1@Password");
        // when
        var loginResponse =
                restTemplate.postForEntity(BASE_URL + "/auth/login", loginRequest, AuthenticationResponse.class);
        // then
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    void loginCredentialsValidationExceptionThrown() {
        // given
        var registrationRequest =
                new RegistrationRequest("john.doe@example.com", "Valid1@Password", "john.doe@example.com");
        restTemplate.postForEntity(
                BASE_URL + "/auth/register", registrationRequest, UserResponse.class);
        var loginRequest = new LoginRequest("john.doe@example.com", "Valid2@Password");
        // when
        var loginResponse =
                restTemplate.postForEntity(BASE_URL + "/auth/login", loginRequest, AuthenticationResponse.class);
        // then
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @SneakyThrows
    @Test
    void requestPasswordResetPositiveTestCase() {
        // given
        var registrationRequest =
                new RegistrationRequest("test@test.me", "Valid1@Password", "test@test.me");
        restTemplate.postForEntity(
                BASE_URL + "/auth/register", registrationRequest, UserResponse.class);
        var requestReset = new EnterPasswordResetRequest("test@test.me");
        // when
        restTemplate.postForEntity(BASE_URL + "/auth/reset-password-request", requestReset, PasswordResetResponse.class);
        // then
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertThat(receivedMessages[1].getSubject().toString().equals("Password reset")).isEqualTo(true);
    }


    @Test
    void resetPasswordPositiveTestCase() {
        // given
        var registrationRequest =
                new RegistrationRequest("test@test.me", "Valid1@Password", "test@test.me");
        var userResponse = restTemplate.postForEntity(
                BASE_URL + "/auth/register", registrationRequest, UserResponse.class);

        var user = userService.findById(userResponse.getBody().uuid());
        var resetRequest = new PasswordResetRequest("Valid2@Password", "Valied2@Password", user.get().getResetToken());
        // when
        var response = restTemplate.postForEntity(BASE_URL + "/auth/reset-password", resetRequest, Void.class);
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    void confirmRegistrationPositiveTestCase() {
        // given
        var registrationRequest =
                new RegistrationRequest("test@test.me", "Valid1@Password", "test@test.me");
        var userResponse = restTemplate.postForEntity(
                BASE_URL + "/auth/register", registrationRequest, UserResponse.class);

        var confirmationRequest = new RegistrationConfirmation(userResponse.getBody().uuid().toString());
        var requestEntity = new HttpEntity<>(confirmationRequest);
        // when
        var response = restTemplate.exchange(BASE_URL + "/auth/confirm-password", HttpMethod.PUT, requestEntity, UserResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().confirmed()).isEqualTo(true);
    }


}
