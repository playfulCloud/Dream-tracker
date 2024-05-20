package com.dreamtracker.app.habit.adapters.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.dreamtracker.app.configuration.TestPostgresConfiguration;
import com.dreamtracker.app.habit.domain.fixtures.HabitFixture;
import com.dreamtracker.app.habit.domain.fixtures.UserFixtures;
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
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;


@Testcontainers
@ContextConfiguration(classes = TestPostgresConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HabitControllerTest implements UserFixtures, HabitFixture {

  @Autowired
  PostgreSQLContainer<?> postgreSQLContainer;

  private final String BASE_URL = "/v1";
  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
  @Autowired TestRestTemplate restTemplate;

  @BeforeEach
  void setUp() {
    restTemplate.postForEntity(BASE_URL + "/seed", null, User.class);
  }

  @Test
  void createHabitPositiveTestCase() {
    var expectedHabitResponse =
        getSampleHabitResponseBuilder(currentUserProvider.getCurrentUser()).build();
    var createdHabitResponse =
        restTemplate.postForEntity(
            BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), HabitResponse.class);

    assertThat(createdHabitResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(createdHabitResponse.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedHabitResponse);
  }

  @Test
  void getAllUserHabitsPositiveTestCase() {
    restTemplate.postForEntity(
            BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), HabitResponse.class);
    var expectedHabitResponse =
            getSampleHabitResponseBuilder(currentUserProvider.getCurrentUser()).build();
    ResponseEntity<Page<HabitResponse>> actualPageResponse = restTemplate.exchange(
            BASE_URL + "/habits",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Page<HabitResponse>>() {}
    );
    assertThat(actualPageResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(actualPageResponse.getBody().getItems().get(0)).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedHabitResponse);
  }
}
