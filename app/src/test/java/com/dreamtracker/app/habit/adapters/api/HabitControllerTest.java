package com.dreamtracker.app.habit.adapters.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.dreamtracker.app.configuration.TestPostgresConfiguration;
import com.dreamtracker.app.habit.domain.fixtures.CategoryFixtures;
import com.dreamtracker.app.habit.domain.fixtures.HabitFixture;
import com.dreamtracker.app.habit.domain.fixtures.HabitTrackFixture;
import com.dreamtracker.app.habit.domain.fixtures.UserFixtures;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import com.dreamtracker.app.user.domain.ports.UserService;
import java.util.UUID;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
@ContextConfiguration(classes = TestPostgresConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HabitControllerTest
    implements UserFixtures, HabitFixture, CategoryFixtures, HabitTrackFixture {

  @Autowired
  PostgreSQLContainer<?> postgreSQLContainer;
  @Autowired
  UserService userService;
  private final String BASE_URL = "/v1";
  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
  private final String wrongUUID = "134cc20c-5f9b-4942-9a13-e23513a26cbb";
  @Autowired TestRestTemplate restTemplate;
  @Autowired
  private DataSource dataSource;


  @BeforeEach
  void setUp() {
    resetDatabase();
    userService.createSampleUser();
  }

  private void resetDatabase() {
    try (var connection = dataSource.getConnection();
         var statement = connection.createStatement()) {
      statement.execute("TRUNCATE TABLE Habit RESTART IDENTITY CASCADE");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void createHabitPositiveTestCase() {
    // given
    var expectedHabitResponse =
        getSampleHabitResponseBuilder(currentUserProvider.getCurrentUser()).build();
    // when
    var createdHabitResponse =
        restTemplate.postForEntity(
            BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), HabitResponse.class);
    // then
    assertThat(createdHabitResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(createdHabitResponse.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedHabitResponse);
  }

  @Test
  void getAllUserHabitsPositiveTestCase() {
    // given
    restTemplate.postForEntity(
        BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), HabitResponse.class);
    var expectedHabitResponse =
            getSampleHabitResponseBuilder(currentUserProvider.getCurrentUser()).build();

    // when
    var actualPageResponse =
        restTemplate.exchange(
            BASE_URL + "/habits",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Page<HabitResponse>>() {});
    // then
    assertThat(actualPageResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(actualPageResponse.getBody().getItems().get(0)).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedHabitResponse);
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
  @Test
  void updateHabitPositiveTestCase(){
    // given
    var habitToUpdated =
        restTemplate
            .postForEntity(
                BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), HabitResponse.class)
            .getBody();
    var habitUpdateRequest = getSampleHabitRequestUpdateBuilder().build();
    var updatedHabit = getSampleUpdatedHabitResponseBuilder().build();
    var requestEntity = new HttpEntity<>(habitUpdateRequest);
    // when
    var updated =
        restTemplate.exchange(
            BASE_URL + "/habits/" + habitToUpdated.id().toString(),
            HttpMethod.PUT,
            requestEntity,
            HabitResponse.class);
    // then
    assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(updated.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(updatedHabit);
  }

  @Test
  void updateHabitHabitNotFound() {
    // given
    var habitToUpdated =
        restTemplate
            .postForEntity(
                BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), HabitResponse.class)
            .getBody();
    var habitUpdateRequest = getSampleHabitRequestUpdateBuilder().build();
    var requestEntity = new HttpEntity<>(habitUpdateRequest);
    // when
    var updated =
        restTemplate.exchange(
            BASE_URL + "/habits/" + wrongUUID, HttpMethod.PUT, requestEntity, HabitResponse.class);
    // then
    assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void deletePositiveTestCase(){
    // given
    var habitToDelete =
        restTemplate
            .postForEntity(
                BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), HabitResponse.class)
            .getBody();
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<?> entity = new HttpEntity<>(headers);
    // when
    var deleted =
        restTemplate.exchange(
            BASE_URL + "/habits/" + habitToDelete.id().toString(),
            HttpMethod.DELETE,
            entity,
            Void.class);
    // then
    assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  void deletePositiveTestCaseHabitNotFound() {
    // given
    var habitToUpdated =
        restTemplate
            .postForEntity(
                BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), HabitResponse.class)
            .getBody();
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<?> entity = new HttpEntity<>(headers);
    // when
    ResponseEntity<Void> updated =
        restTemplate.exchange(
            BASE_URL + "/habits/" + wrongUUID, HttpMethod.DELETE, entity, Void.class);
    // then
    assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void linkHabitWithCategoryPositiveTestCase(){
    // given
    var categoryToBeLinked = getSampleCategoryRequestBuilder().build();
    var createdCategory = restTemplate.postForEntity(BASE_URL+"/categories",categoryToBeLinked,CategoryResponse.class);
    var habitToLink =
        restTemplate
            .postForEntity(
                BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), HabitResponse.class)
            .getBody();
    // when
    var response =
        restTemplate.postForEntity(
            BASE_URL + "/habits/" + habitToLink.id().toString() + "/categories",
            HabitCategoryCreateRequest.builder().id(createdCategory.getBody().id()).build(),
            Void.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  void linkHabitWithCategoryHabitNotFound() {
    // given
    var categoryToBeLinked = getSampleCategoryRequestBuilder().build();
    var createdCategory =
        restTemplate.postForEntity(
            BASE_URL + "/categories", categoryToBeLinked, CategoryResponse.class);
    var habitToLink =
        restTemplate
            .postForEntity(
                BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), HabitResponse.class)
            .getBody();
    // when
    var response =
        restTemplate.postForEntity(
            BASE_URL + "/habits/" + wrongUUID + "/categories",
            HabitCategoryCreateRequest.builder().id(createdCategory.getBody().id()).build(),
            Void.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void linkHabitWithCategoryCategoryNotFound() {
    // given
    var habitToLink =
        restTemplate
            .postForEntity(
                BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), HabitResponse.class)
            .getBody();
    // when
    var response =
        restTemplate.postForEntity(
            BASE_URL + "/habits/" + habitToLink.id().toString() + "/categories",
            HabitCategoryCreateRequest.builder().id(UUID.fromString(wrongUUID)).build(),
            Void.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void trackTheHabitPositiveTestCase() {
    // given
    var habitToTrack =
        restTemplate
            .postForEntity(
                BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), HabitResponse.class)
            .getBody();
    var habitTrackRequest = getSampleHabitTrackRequest(habitToTrack.id()).build();
    var expectedHabitTrackResponse = getSampleHabitTrackResponse("somedate");
    // when
    var habitTrackResponse =
        restTemplate.postForEntity(
            BASE_URL + "/habits-tracking", habitTrackRequest, HabitTrackResponse.class);
    // then
    assertThat(habitTrackResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(habitTrackResponse.getBody())
        .usingRecursiveComparison()
        .ignoringFields("date")
        .isEqualTo(expectedHabitTrackResponse);
  }

  @Test
  void trackTheHabitHabitNotFound() {
    // given
    var habitToTrack =
        restTemplate
            .postForEntity(
                BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), HabitResponse.class)
            .getBody();
    var habitTrackRequest = getSampleHabitTrackRequest(UUID.fromString(wrongUUID)).build();
    // when
    var habitTrackResponse =
        restTemplate.postForEntity(
            BASE_URL + "/habits-tracking", habitTrackRequest, HabitTrackResponse.class);
    // then
    assertThat(habitTrackResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }


  @Test
  void getHabitByIdPositiveTestCase(){
    // given
    var habitToFind =
            restTemplate
                    .postForEntity(
                            BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), HabitResponse.class)
                    .getBody();
    var expectedHabitResponse = getSampleHabitResponseBuilder(currentUserProvider.getCurrentUser()).build();
    // when
    var habitResponse = restTemplate.getForEntity(BASE_URL + "/habits/" + habitToFind.id().toString(),HabitResponse.class);
    // then
    assertThat(habitResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(habitResponse.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedHabitResponse);
  }

  @Test
  void getHabitByIdHabitNotFound(){
    // given
    var habitToFind =
            restTemplate
                    .postForEntity(
                            BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), HabitResponse.class)
                    .getBody();
    // when
    var habitResponse = restTemplate.getForEntity(BASE_URL + "/habits/" + wrongUUID,HabitResponse.class);

    // then
    assertThat(habitResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void getAllHabitTracksPositiveTestCase(){
    var habitToTrack =
            restTemplate
                    .postForEntity(
                            BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), HabitResponse.class)
                    .getBody();
    var habitTrackRequest = getSampleHabitTrackRequest(habitToTrack.id()).build();
    // when
    var habitTrackResponse =
            restTemplate.postForEntity(
                    BASE_URL + "/habits-tracking", habitTrackRequest, HabitTrackResponse.class);

    var actualPageResponse =
            restTemplate.exchange(
                    BASE_URL + "/habits-tracking/" + habitToTrack.id(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Page<HabitTrackResponse>>() {});
    // then
    assertThat(actualPageResponse.getBody().getItems().get(0)).usingRecursiveComparison().ignoringFields("id").isEqualTo(habitTrackResponse.getBody());
  }

  @Test
  void getAllHabitTracksEmptyPage(){
    // given
    var habitToTrack =
            restTemplate
                    .postForEntity(
                            BASE_URL + "/habits", getSampleHabitRequestBuilder().build(), HabitResponse.class)
                    .getBody();
    var habitTrackRequest = getSampleHabitTrackRequest(habitToTrack.id()).build();
    // when
    var actualPageResponse =
            restTemplate.exchange(
                    BASE_URL + "/habits-tracking/" + wrongUUID,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Page<HabitTrackResponse>>() {});
    // then
    assertThat(actualPageResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(actualPageResponse.getBody().getItems().size()).isEqualTo(0);
  }
}
