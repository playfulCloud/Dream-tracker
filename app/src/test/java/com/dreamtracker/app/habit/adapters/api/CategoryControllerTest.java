package com.dreamtracker.app.habit.adapters.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.dreamtracker.app.configuration.TestPostgresConfiguration;
import com.dreamtracker.app.fixtures.CategoryFixtures;
import com.dreamtracker.app.infrastructure.auth.AuthenticationResponse;
import com.dreamtracker.app.infrastructure.auth.LoginRequest;
import com.dreamtracker.app.infrastructure.auth.RegistrationRequest;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.user.adapters.api.UserResponse;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import com.dreamtracker.app.user.domain.ports.UserService;
import java.util.ArrayList;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
@ContextConfiguration(classes = TestPostgresConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CategoryControllerTest implements CategoryFixtures {
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
    registerAndLoginUser();
  }

  private void resetDatabase() {
    try (var connection = dataSource.getConnection();
         var statement = connection.createStatement()) {
      statement.execute("TRUNCATE TABLE Categories RESTART IDENTITY CASCADE");
      statement.execute("TRUNCATE TABLE Goals RESTART IDENTITY CASCADE");
      statement.execute("TRUNCATE TABLE Habits RESTART IDENTITY CASCADE");
      statement.execute("TRUNCATE TABLE Users RESTART IDENTITY CASCADE");
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
    assertThat(registrationResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    var loginRequest = new LoginRequest("john.doe@example.com", "Doe");
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
  void getAllUserCategoriesEmptyPage() {
    // given
    var expectedCategoryResponse = getSampleCategoryBuilder(currentUserProvider.getCurrentUser());
    // when
    var actualPageResponse =
            restTemplate.exchange(
                    BASE_URL + "/categories",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Page<CategoryResponse>>() {});
    // then
    assertThat(actualPageResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(actualPageResponse.getBody())
            .isEqualTo(new Page<CategoryResponse>(new ArrayList<>()));
  }

  @Test
  void createCategoryPositiveTestCase() {
    // given
    var categoryRequest = getSampleCategoryRequestBuilder().build();
    var expectedCategoryResponse = getSampleCategoryBuilder(currentUserProvider.getCurrentUser());
    // when
    var actualCategoryResponse =
        restTemplate.postForEntity(
            BASE_URL + "/categories", categoryRequest, CategoryResponse.class);
    // then
    assertThat(actualCategoryResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(actualCategoryResponse.getBody())
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(expectedCategoryResponse);
  }

  @Test
  void getAllUserCategoriesPositiveTestCase() {
    // given
    var category =
        restTemplate.postForEntity(
            BASE_URL + "/categories",
            getSampleCategoryRequestBuilder().build(),
            HabitResponse.class);
    var expectedCategoryResponse = getSampleCategoryBuilder(currentUserProvider.getCurrentUser());
    // when
    var actualPageResponse =
        restTemplate.exchange(
            BASE_URL + "/categories",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Page<CategoryResponse>>() {});
    // then
    assertThat(actualPageResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(actualPageResponse.getBody().getItems().get(0))
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(expectedCategoryResponse);
  }



  @Test
  void updateCategoryPositiveTestCase() {
    // given
    var categoryToUpdated =
        restTemplate
            .postForEntity(
                BASE_URL + "/categories",
                getSampleUpdateCategoryRequestBuilder().build(),
                HabitResponse.class)
            .getBody();
    var categoryUpdateRequest = getSampleUpdateCategoryRequestBuilder().build();
    var updatedCategory =
        getSampleUpdatedCategoryBuilder(currentUserProvider.getCurrentUser()).build();
    var requestEntity = new HttpEntity<>(categoryUpdateRequest);
    // when
    var updated =
        restTemplate.exchange(
            BASE_URL + "/categories/" + categoryToUpdated.id().toString(),
            HttpMethod.PUT,
            requestEntity,
            CategoryResponse.class);
    // then
    assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(updated.getBody())
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(updatedCategory);
  }

  @Test
  void updateCategoryNotFound() {
    // given
    var categoryUpdateRequest = getSampleUpdateCategoryRequestBuilder().build();
    var requestEntity = new HttpEntity<>(categoryUpdateRequest);
    // when
    var updated =
        restTemplate.exchange(
            BASE_URL + "/categories/" + wrongUUID,
            HttpMethod.PUT,
            requestEntity,
            CategoryResponse.class);
    // then
    assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void deletePositiveTestCase() {
    // given
    var categoryToDelete =
        restTemplate
            .postForEntity(
                BASE_URL + "/categories",
                getSampleCategoryBuilder(currentUserProvider.getCurrentUser()).build(),
                HabitResponse.class)
            .getBody();
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<?> entity = new HttpEntity<>(headers);
    // when
    var deleted =
        restTemplate.exchange(
            BASE_URL + "/categories/" + categoryToDelete.id().toString(),
            HttpMethod.DELETE,
            entity,
            Void.class);
    // then
    assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  void deleteCategoryNotFound() {
    // given
    var categoryToDelete =
        restTemplate
            .postForEntity(
                BASE_URL + "/categories",
                getSampleCategoryBuilder(currentUserProvider.getCurrentUser()).build(),
                HabitResponse.class)
            .getBody();
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<?> entity = new HttpEntity<>(headers);
    // when
    var deleted =
        restTemplate.exchange(
            BASE_URL + "/categories/" + categoryToDelete.id().toString(),
            HttpMethod.DELETE,
            entity,
            Void.class);
    // then
    assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }
}
