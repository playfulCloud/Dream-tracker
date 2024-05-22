package com.dreamtracker.app.habit.adapters.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.dreamtracker.app.configuration.TestPostgresConfiguration;
import com.dreamtracker.app.habit.domain.fixtures.CategoryFixtures;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import com.dreamtracker.app.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ContextConfiguration(classes = TestPostgresConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest implements CategoryFixtures {
  @Autowired
  PostgreSQLContainer<?> postgreSQLContainer;
  private final String BASE_URL = "/v1";
  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
  private final String wrongUUID = "134cc20c-5f9b-4942-9a13-e23513a26cbb";
  @Autowired TestRestTemplate restTemplate;

  @BeforeEach
  void setUp() {
    // populating database with initial user need to performing habit operations
    // in some test there is also call to create a category it is also needed to perform testing
    restTemplate.postForEntity(BASE_URL + "/seed", null, User.class);
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
