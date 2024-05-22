package com.dreamtracker.app.goal.adapters.api;

import com.dreamtracker.app.configuration.TestPostgresConfiguration;
import com.dreamtracker.app.goal.domain.fixtures.GoalFixtures;
import com.dreamtracker.app.habit.adapters.api.HabitResponse;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ContextConfiguration(classes = TestPostgresConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GoalControllerTest implements GoalFixtures {
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
                    BASE_URL + "/goals", getSampleGoalRequestBuilder().build(), HabitResponse.class);
    // then
    assertThat(createdGoalResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(createdGoalResponse.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedGoalResponse);
  }


  @Test
  void getAllUserGoalsPositiveTestCase() {
    // given
    restTemplate.postForEntity(
            BASE_URL + "/habits", getSampleGoalRequestBuilder().build(), HabitResponse.class);
    var expectedGoalResponse =
            getExpectedGoalResponse().build();

    // when
    var actualPageResponse =
            restTemplate.exchange(
                    BASE_URL + "/goals",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Page<HabitResponse>>() {});
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
                    BASE_URL + "/habits",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Page<HabitResponse>>() {});
    // then
    assertThat(actualPageResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(actualPageResponse.getBody().getItems().size()).isEqualTo(0);
  }
}
