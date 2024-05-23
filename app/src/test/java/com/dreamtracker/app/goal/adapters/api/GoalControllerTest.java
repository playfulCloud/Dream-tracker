package com.dreamtracker.app.goal.adapters.api;

import com.dreamtracker.app.configuration.TestPostgresConfiguration;
import com.dreamtracker.app.goal.domain.fixtures.GoalFixtures;
import com.dreamtracker.app.habit.adapters.api.CategoryResponse;
import com.dreamtracker.app.habit.adapters.api.GoalAssignHabitRequest;
import com.dreamtracker.app.habit.adapters.api.HabitCategoryCreateRequest;
import com.dreamtracker.app.habit.adapters.api.HabitResponse;
import com.dreamtracker.app.habit.domain.fixtures.HabitFixture;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import com.dreamtracker.app.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ContextConfiguration(classes = TestPostgresConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GoalControllerTest implements GoalFixtures, HabitFixture {
  @Autowired
  PostgreSQLContainer<?> postgreSQLContainer;
  private final String BASE_URL = "/v1";
  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
  private final String wrongUUID = "134cc20c-5f9b-4942-9a13-e23513a26cbb";
  @Autowired
  TestRestTemplate restTemplate;

  @BeforeEach
  void setUp() {
    // populating database with initial user need to performing habit operations
    // in same test there is also call to create a goal it is also needed to perform testing
    restTemplate.postForEntity(BASE_URL + "/seed", null, User.class);
  }

  @Test
  void createHabitPositiveTestCase() {
    // given
    var expectedGoalResponse =
            getExpectedGoalResponse().build();
    // when
    var createdGoalResponse =
            restTemplate.postForEntity(
                    BASE_URL + "/goals", getSampleGoalRequestBuilder().build(), GoalResponse.class);
    // then
    assertThat(createdGoalResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(createdGoalResponse.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedGoalResponse);
  }


  @Test
  void getAllUserGoalsPositiveTestCase() {
    // given
    restTemplate.postForEntity(
            BASE_URL + "/habits", getSampleGoalRequestBuilder().build(), GoalResponse.class);
    var expectedGoalResponse =
            getExpectedGoalResponse().build();

    // when
    var actualPageResponse =
            restTemplate.exchange(
                    BASE_URL + "/goals",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Page<GoalResponse>>() {});
    // then
    assertThat(actualPageResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(actualPageResponse.getBody().getItems().get(0)).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedGoalResponse);
  }

  @Test
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
    assertThat(actualPageResponse.getBody().getItems().size()).isEqualTo(0);
  }


  @Test
  void updateGoalPositiveTestCase(){
    // given
    var goalToUpdated =
            restTemplate
                    .postForEntity(
                            BASE_URL + "/goals", getSampleGoalRequestBuilder().build(), HabitResponse.class)
                    .getBody();
    var goalUpdateRequest = getSampleUpdateGoalRequestBuilder().build();
    var updatedGoal = getUpdatedExpectedGoalResponse();
    var requestEntity = new HttpEntity<>(goalUpdateRequest);
    // when
    var updated =
            restTemplate.exchange(
                    BASE_URL + "/goals/" + goalToUpdated.id().toString(),
                    HttpMethod.PUT,
                    requestEntity,
                    GoalResponse.class);
    // then
    assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(updated.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(updatedGoal);
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
    var goalToDelete =
            restTemplate
                    .postForEntity(
                            BASE_URL + "/goals", getSampleGoalRequestBuilder().build(), HabitResponse.class)
                    .getBody();
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<?> entity = new HttpEntity<>(headers);
    // when
    var deleted =
            restTemplate.exchange(
                    BASE_URL + "/goals/" + goalToDelete.id().toString(),
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

  @Test
  void associateGoalWithHabitPositiveTestCase(){
    // given
    var goalToAdd = getSampleGoalRequestBuilder().build();
    var createdGoal = restTemplate.postForEntity(BASE_URL+"/goals",goalToAdd, GoalResponse.class).getBody();
    var habitToBeAdded =
            restTemplate
                    .postForEntity(
                            BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), HabitResponse.class)
                    .getBody();
    // when
    var response =
            restTemplate.postForEntity(
                    BASE_URL + "/goals/" + createdGoal.id() + "/habits",
                    GoalAssignHabitRequest.builder().habitId(habitToBeAdded.id()).completionCount(19).build(),
                    Void.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  void associateGoalWithHabitHabitNotFound(){
    // given
    var goalToAdd = getSampleGoalRequestBuilder().build();
    var createdGoal = restTemplate.postForEntity(BASE_URL+"/goals",goalToAdd, GoalResponse.class).getBody();

    // when
    var response =
            restTemplate.postForEntity(
                    BASE_URL + "/goals/" + createdGoal.id().toString() + "/habits",
                    GoalAssignHabitRequest.builder().habitId(UUID.fromString(wrongUUID)).build(),
                    Void.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void associateGoalWithHabitGoalNotFound(){
    // given
    var habitToBeAdded =
            restTemplate
                    .postForEntity(
                            BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), HabitResponse.class)
                    .getBody();
    // when
    var response =
            restTemplate.postForEntity(
                    BASE_URL + "/goals/" + wrongUUID + "/habits",
                    GoalAssignHabitRequest.builder().habitId(habitToBeAdded.id()).build(),
                    Void.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }
}

