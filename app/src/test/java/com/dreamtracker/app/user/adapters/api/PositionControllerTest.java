package com.dreamtracker.app.user.adapters.api;

import com.dreamtracker.app.configuration.TestPostgresConfiguration;
import com.dreamtracker.app.fixtures.PositionFixtures;
import com.dreamtracker.app.infrastructure.auth.AuthenticationResponse;
import com.dreamtracker.app.infrastructure.auth.LoginRequest;
import com.dreamtracker.app.infrastructure.auth.RegistrationRequest;
import com.dreamtracker.app.user.domain.ports.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ContextConfiguration(classes = TestPostgresConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PositionControllerTest implements PositionFixtures {


    @Autowired
    PostgreSQLContainer<?> postgreSQLContainer;
    @Autowired
    UserService userService;
    private final String BASE_URL = "/v1";
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    private DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(PositionControllerTest.class);



    @BeforeEach
    void setUp() {
        resetDatabase();
        registerAndLoginUser();
    }

    private void resetDatabase() {
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE Positions RESTART IDENTITY CASCADE");
            statement.execute("TRUNCATE TABLE Users RESTART IDENTITY CASCADE");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void registerAndLoginUser() {
        var registrationRequest =
                new RegistrationRequest("john.doe@example.com", "Valid1@Password", "john.doe@example.com");
        var registrationResponse =
                restTemplate.postForEntity(
                        BASE_URL + "/auth/register", registrationRequest, UserResponse.class);
        assertThat(registrationResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        var loginRequest = new LoginRequest("john.doe@example.com", "Valid1@Password");
        var loginResponse =
                restTemplate.postForEntity(BASE_URL + "/login", loginRequest, AuthenticationResponse.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);


        var authToken = loginResponse.getBody().token();
        restTemplate.getRestTemplate().getInterceptors().clear();
        restTemplate
                .getRestTemplate()
                .getInterceptors()
                .add(
                        (request, body, execution) -> {
                            request.getHeaders().add("Authorization", "Bearer " + authToken);
                            return execution.execute(request, body);
                        });
    }


    @Test
    void testGettingCreatedPosition() {
        // given
        var positionRequest = getPositionRequest().build();
        restTemplate.postForEntity(BASE_URL + "/positions", positionRequest, PositionResponse.class);

        var expectedPositionResponse = getPositionResponse().build();

        // when
        var response = restTemplate.exchange(BASE_URL + "/positions", HttpMethod.GET, null, new ParameterizedTypeReference<PositionResponse>() {
        });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedPositionResponse);
    }

    @Test
    void createPosition() {
        // given
        var expectedPositionResponse = getPositionResponse().build();
        var positionRequest = getPositionRequest().build();

        logger.info(expectedPositionResponse.toString());
        logger.info(positionRequest.toString());

        // when
        var actualPosition = restTemplate.postForEntity(BASE_URL + "/positions", positionRequest, PositionResponse.class);

        logger.error(actualPosition.toString());
        // then
        assertThat(actualPosition.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actualPosition.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedPositionResponse);
    }

    @Test
    void testChangePosition() {
        // given
        var positionRequest = getPositionRequest().build();
        restTemplate.postForEntity(BASE_URL + "/positions", positionRequest, PositionResponse.class);
        var changeRequest = getPositionChangeRequest().goalX(15).goalY(15).build();
        var requestEntity = new HttpEntity<>(changeRequest);
        var expectedPositionResponse = getPositionResponse().goalX(15).goalY(15).build();


        // when
        var changedPosition = restTemplate.exchange(BASE_URL + "/position-change", HttpMethod.PUT, requestEntity, PositionResponse.class);

        // then
        assertThat(changedPosition.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(changedPosition.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedPositionResponse);
    }

    @Test
    void testChangeActivation() {
        // given
        var positionRequest = getPositionRequest().build();
        restTemplate.postForEntity(BASE_URL + "/positions", positionRequest, PositionResponse.class);
        var activationRequest = getActivationRequest().chartsEnabled(true).build();
        var requestEntity = new HttpEntity<>(activationRequest);
        var expectedPositionResponse = getPositionResponse().chartsEnabled(true).build();

        // when
        var changedPosition = restTemplate.exchange(BASE_URL + "/position-activation", HttpMethod.PUT, requestEntity, PositionResponse.class);

        // then
        assertThat(changedPosition.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(changedPosition.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedPositionResponse);
    }
}