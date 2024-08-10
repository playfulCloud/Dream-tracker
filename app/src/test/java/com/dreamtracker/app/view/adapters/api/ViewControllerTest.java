package com.dreamtracker.app.view.adapters.api;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.dreamtracker.app.configuration.TestPostgresConfiguration;
import com.dreamtracker.app.fixtures.HabitFixture;
import com.dreamtracker.app.fixtures.ViewFixture;
import com.dreamtracker.app.habit.adapters.api.HabitResponse;
import com.dreamtracker.app.infrastructure.auth.AuthenticationResponse;
import com.dreamtracker.app.infrastructure.auth.LoginRequest;
import com.dreamtracker.app.infrastructure.auth.RegistrationRequest;
import com.dreamtracker.app.user.adapters.api.UserResponse;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import com.dreamtracker.app.user.domain.ports.UserService;
import java.util.UUID;
import javax.sql.DataSource;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ContextConfiguration(classes = TestPostgresConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ViewControllerTest implements ViewFixture, HabitFixture {

  @Autowired PostgreSQLContainer<?> postgreSQLContainer;
  @Autowired UserService userService;
  private final String BASE_URL = "/v1";
  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
  private final String wrongUUID = "134cc20c-5f9b-4942-9a13-e23513a26cbb";
  private static final Logger logger = LoggerFactory.getLogger(ViewControllerTest.class);
  @Autowired TestRestTemplate restTemplate;
  @Autowired private DataSource dataSource;

  @BeforeEach
  void setUp() {
    resetDatabase();
    registerAndLoginUser();
  }

  private void resetDatabase() {
    try (var connection = dataSource.getConnection();
        var statement = connection.createStatement()) {
      statement.execute("TRUNCATE TABLE View RESTART IDENTITY CASCADE");
      statement.execute("TRUNCATE TABLE category RESTART IDENTITY CASCADE");
      statement.execute("TRUNCATE TABLE Goal RESTART IDENTITY CASCADE");
      statement.execute("TRUNCATE TABLE Habit RESTART IDENTITY CASCADE");
      statement.execute("TRUNCATE TABLE users RESTART IDENTITY CASCADE");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void registerAndLoginUser() {
    var registrationRequest =
            new RegistrationRequest("john.doe@example.com", "Doe", "john.doe@example.com");
    var registrationResponse =
            restTemplate.postForEntity(
                    BASE_URL + "/auth/register", registrationRequest, UserResponse.class);
    Assertions.assertThat(registrationResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    var loginRequest = new LoginRequest("john.doe@example.com", "Doe");
    var loginResponse =
            restTemplate.postForEntity(BASE_URL + "/login", loginRequest, AuthenticationResponse.class);
    Assertions.assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

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
  void getCalculatedStats() {
    // given
    var habitRequest = getSampleHabitRequestBuilder().build();
    var createdHabit =
        restTemplate.postForEntity(BASE_URL + "/habits", habitRequest, HabitResponse.class);

    // when
    var actualGetStatsResponse =
        restTemplate.getForEntity(
            BASE_URL + "/views/stats/" + createdHabit.getBody().id(),
            CombinedComponentResponse.class);

    // then
    assertThat(actualGetStatsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(actualGetStatsResponse.getBody())
        .isEqualTo(
            CombinedComponentResponse.builder()
                .date(actualGetStatsResponse.getBody().getDate())
                .trend("STAGNATION")
                .build());
  }

  @Test
  void getCalculatedStatsEntityNotFound() {
    // given
    // when
    var actualGetStatsResponse =
        restTemplate.getForEntity(
            BASE_URL + "/views/stats/" + wrongUUID, CombinedComponentResponse.class);

    // then
    assertThat(actualGetStatsResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void getViewByName() {
    // given
    var expectedViewResponse =
        getViewResponseBuilder()
            .userUUID(currentUserProvider.getCurrentUser())
            .name("testName")
            .build();
    var viewRequest = getViewRequestBuilder().name("testName").build();
    // when

    restTemplate.postForEntity(BASE_URL + "/views", viewRequest, ViewResponse.class);

    var actualGetByNameResponse =
        restTemplate.getForEntity(BASE_URL + "/views/" + viewRequest.name(), ViewResponse.class);
    // then
    assertThat(actualGetByNameResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(expectedViewResponse)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(actualGetByNameResponse.getBody());
  }

  @Test
  void getViewByNameEntityNotFound() {
    // given
    var viewRequest = getViewRequestBuilder().build();
    // when
    restTemplate.postForEntity(BASE_URL + "/views", viewRequest, ViewResponse.class);

    var actualGetByNameResponse =
        restTemplate.getForEntity(BASE_URL + "/views/" + "wrongName", ViewResponse.class);
    // then
    assertThat(actualGetByNameResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void createView() {
    // given
    var expectedViewResponse =
        getViewResponseBuilder().userUUID(currentUserProvider.getCurrentUser()).build();
    var viewRequest = getViewRequestBuilder().build();
    // when
    var actualViewResponse =
        restTemplate.postForEntity(BASE_URL + "/views", viewRequest, ViewResponse.class);
    // then
    assertThat(actualViewResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(expectedViewResponse)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(actualViewResponse.getBody());
  }

  @Test
  void updateView() {
    // given
    var viewRequest = getViewRequestBuilder().description("prechanged").build();
    var expectedViewResponse =
        getViewResponseBuilder()
            .userUUID(currentUserProvider.getCurrentUser())
            .description("changed")
            .build();
    var createdView =
        restTemplate.postForEntity(BASE_URL + "/views", viewRequest, ViewResponse.class);

    var viewUpdateRequest =
        getViewRequestBuilder().id(createdView.getBody().id()).description("changed").build();
    var requestEntity = new HttpEntity<>(viewUpdateRequest);

    // when
    var actualViewResponse =
        restTemplate.exchange(
            BASE_URL + "/views", HttpMethod.PUT, requestEntity, ViewResponse.class);

    // then
    assertThat(actualViewResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(expectedViewResponse)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(actualViewResponse.getBody());
  }

  @Test
  void updateViewEntityNotFound() {
    // given
    var viewUpdateRequest =
        getViewRequestBuilder().id(UUID.fromString(wrongUUID)).description("changed").build();
    var requestEntity = new HttpEntity<>(viewUpdateRequest);
    // when
    var actualViewResponse =
        restTemplate.exchange(
            BASE_URL + "/views", HttpMethod.PUT, requestEntity, ViewResponse.class);
    // then
    assertThat(actualViewResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void deleteView() {

    var viewRequest = getViewRequestBuilder().build();

    var actualViewResponse =
        restTemplate.postForEntity(BASE_URL + "/views", viewRequest, ViewResponse.class);

    var headers = new HttpHeaders();
    var requestEntity = new HttpEntity<>(headers);

    var actualViewResponseDelete =
        restTemplate.exchange(
            BASE_URL + "/views/" + actualViewResponse.getBody().id().toString(),
            HttpMethod.DELETE,
            requestEntity,
            Void.class);

    assertThat(actualViewResponseDelete.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  void deleteViewEntityNotFound() {

    // given
    var headers = new HttpHeaders();
    var requestEntity = new HttpEntity<>(headers);

    // when
    var actualViewResponseDelete =
        restTemplate.exchange(
            BASE_URL + "/views/" + UUID.fromString(wrongUUID),
            HttpMethod.DELETE,
            requestEntity,
            Void.class);

    // then
    assertThat(actualViewResponseDelete.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }
}
