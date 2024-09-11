package com.dreamtracker.app.goal.adapters.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.dreamtracker.app.configuration.TestPostgresConfiguration;
import com.dreamtracker.app.fixtures.GoalFixtures;
import com.dreamtracker.app.fixtures.HabitFixture;
import com.dreamtracker.app.goal.domain.model.GoalStatus;
import com.dreamtracker.app.habit.adapters.api.HabitResponse;
import com.dreamtracker.app.infrastructure.auth.AuthenticationResponse;
import com.dreamtracker.app.infrastructure.auth.LoginRequest;
import com.dreamtracker.app.infrastructure.auth.RegistrationRequest;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.user.adapters.api.UserResponse;
import com.dreamtracker.app.user.domain.ports.UserService;
import java.util.ArrayList;
import javax.sql.DataSource;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ContextConfiguration(classes = TestPostgresConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GoalControllerTest implements GoalFixtures, HabitFixture {
  @Autowired
  PostgreSQLContainer<?> postgreSQLContainer;
  @Autowired
  UserService userService;
  private final String BASE_URL = "/v1";
  private final String wrongUUID = "134cc20c-5f9b-4942-9a13-e23513a26cbb";
  @Autowired
  TestRestTemplate restTemplate;
  @Autowired
  private DataSource dataSource;

  private static final Logger logger = LoggerFactory.getLogger(GoalControllerTest.class);

  @BeforeEach
  void setUp() {
    resetDatabase();
    registerAndLoginUser();
  }

  private void resetDatabase() {
    try (var connection = dataSource.getConnection();
         var statement = connection.createStatement()) {
      statement.execute("TRUNCATE TABLE Goals RESTART IDENTITY CASCADE");
      statement.execute("TRUNCATE TABLE Habits RESTART IDENTITY CASCADE");
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
        restTemplate.postForEntity(BASE_URL + "/auth/login", loginRequest, AuthenticationResponse.class);
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
  void createHabitPositiveTestCase() {
    // given
    var createdHabitResponse =
        restTemplate.postForEntity(
            BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), GoalResponse.class);

    var expectedGoalResponse =
        getExpectedGoalResponse()
            .habitID(createdHabitResponse.getBody().id())
            .completionCount(10)
                .status(GoalStatus.ACTIVE.toString())
            .build();
    // when
    var createdGoalResponse =
        restTemplate.postForEntity(
            BASE_URL + "/goals",
            getSampleGoalRequestBuilder().habitID(createdHabitResponse.getBody().id()).build(),
            GoalResponse.class);
    // then
    assertThat(createdGoalResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(createdGoalResponse.getBody()).usingRecursiveComparison().ignoringFields("id","createdAt").isEqualTo(expectedGoalResponse);
  }


  @Test
  void getAllUserGoalsPositiveTestCase() {

    // given
    var createdHabitResponse =
        restTemplate.postForEntity(
            BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), GoalResponse.class);

    var expectedGoalResponse =
        getExpectedGoalResponse()
            .habitID(createdHabitResponse.getBody().id())
            .completionCount(10)
                .status(GoalStatus.ACTIVE.toString())
            .build();

    restTemplate.postForEntity(
        BASE_URL + "/goals",
        getSampleGoalRequestBuilder().habitID(createdHabitResponse.getBody().id()).build(),
        GoalResponse.class);
    restTemplate.postForEntity(
            BASE_URL + "/goals", getSampleGoalRequestBuilder().build(), GoalResponse.class);


    // when
    var actualPageResponse =
            restTemplate.exchange(
                    BASE_URL + "/goals",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Page<GoalResponse>>() {});
    // then
    assertThat(actualPageResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(actualPageResponse.getBody().getItems().get(0)).usingRecursiveComparison().ignoringFields("id","createdAt").isEqualTo(expectedGoalResponse);
  }

  @Test
  @Order(1)
  void getAllUserHabitsEmptyPage() {
    // given
    // when
    var actualPageResponse =
            restTemplate.exchange(
                    BASE_URL + "/goals",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Page<GoalResponse>>() {});
    // then
    assertThat(actualPageResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(actualPageResponse.getBody()).isEqualTo(new Page<GoalResponse>(new ArrayList<>()));
  }


  @Test
  void updateGoalPositiveTestCase(){

  // given
    var createdHabitResponse =
        restTemplate.postForEntity(
            BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), GoalResponse.class);

    var expectedGoalResponse =
        getExpectedGoalResponse()
            .habitID(createdHabitResponse.getBody().id())
            .completionCount(10)
            .build();
    // when
    var createdGoalResponse =
        restTemplate.postForEntity(
            BASE_URL + "/goals",
            getSampleGoalRequestBuilder().habitID(createdHabitResponse.getBody().id()).completionCount(10).build(),
            GoalResponse.class);

    var goalUpdateRequest =
        getSampleUpdateGoalRequestBuilder().habitID(createdHabitResponse.getBody().id()).completionCount(createdHabitResponse.getBody().completionCount()).build();
    var updatedGoal = getUpdatedExpectedGoalResponse().habitID(createdHabitResponse.getBody().id()).completionCount(10).status(GoalStatus.ACTIVE.toString()).build();
    var requestEntity = new HttpEntity<>(goalUpdateRequest);
    // when
    var updated =
            restTemplate.exchange(
                    BASE_URL + "/goals/" + createdGoalResponse.getBody().id().toString(),
                    HttpMethod.PUT,
                    requestEntity,
                    GoalResponse.class);
    // then
    assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(updated.getBody()).usingRecursiveComparison().ignoringFields("id","createdAt").isEqualTo(updatedGoal);
  }

  @Test
  void updateGoalGoalNotFound() {
    // given
    var goalToUpdated =
            restTemplate
                    .postForEntity(
                            BASE_URL + "/goals", getSampleGoalRequestBuilder().build(), GoalResponse.class)
                    .getBody();
    var goalUpdateRequest = getSampleUpdateGoalRequestBuilder().build();
    var requestEntity = new HttpEntity<>(goalUpdateRequest);
    // when
    var updated =
            restTemplate.exchange(
                    BASE_URL + "/goals/" + wrongUUID, HttpMethod.PUT, requestEntity, GoalResponse.class);
    // then
    assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void deletePositiveTestCase(){
    // given
    var createdHabitResponse =
            restTemplate.postForEntity(
                    BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), GoalResponse.class);

    var expectedGoalResponse =
            getExpectedGoalResponse()
                    .habitID(createdHabitResponse.getBody().id())
                    .completionCount(10)
                    .build();
    // when
    var createdGoalResponse =
            restTemplate.postForEntity(
                    BASE_URL + "/goals",
                    getSampleGoalRequestBuilder().habitID(createdHabitResponse.getBody().id()).build(),
                    GoalResponse.class);

    System.out.println(createdGoalResponse);
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<?> entity = new HttpEntity<>(headers);
    // when
    var deleted =
            restTemplate.exchange(
                    BASE_URL + "/goals/" +createdGoalResponse.getBody().id().toString(),
                    HttpMethod.DELETE,
                    entity,
                    Void.class);
    // then
    assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  void deletePositiveTestCaseHabitNotFound() {
    // given
    var goalToUpdated =
            restTemplate
                    .postForEntity(
                            BASE_URL + "/goals", getSampleGoalRequestBuilder().build(), HabitResponse.class)
                    .getBody();
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<?> entity = new HttpEntity<>(headers);
    // when
    ResponseEntity<Void> updated =
            restTemplate.exchange(
                    BASE_URL + "/goals/" + wrongUUID, HttpMethod.DELETE, entity, Void.class);
    // then
    assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

}

