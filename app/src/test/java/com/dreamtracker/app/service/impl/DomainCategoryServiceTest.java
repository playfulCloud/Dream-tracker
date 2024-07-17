package com.dreamtracker.app.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.dreamtracker.app.habit.adapters.api.CategoryResponse;
import com.dreamtracker.app.fixtures.CategoryFixtures;
import com.dreamtracker.app.fixtures.UserFixtures;
import com.dreamtracker.app.habit.domain.model.Category;
import com.dreamtracker.app.habit.domain.ports.CategoryRepositoryPort;
import com.dreamtracker.app.habit.domain.ports.CategoryService;
import com.dreamtracker.app.habit.domain.ports.DomainCategoryService;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import com.dreamtracker.app.user.domain.model.User;
import com.dreamtracker.app.user.domain.ports.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DomainCategoryServiceTest implements CategoryFixtures, UserFixtures {

  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
  private final CategoryRepositoryPort categoryRepositoryPort = Mockito.mock(CategoryRepositoryPort.class);
  private final UserService userService = Mockito.mock(UserService.class);
  private User sampleUser;
  private CategoryService categoryService;

  @BeforeEach
  void setUp() {
    sampleUser = getSampleUser(currentUserProvider.getCurrentUser()).build();
    categoryService = new DomainCategoryService(categoryRepositoryPort, userService, currentUserProvider);
  }

  @Test
  void createCategoryPositiveTestCase() {
    // given
    var sampleCategory = getSampleCategoryBuilder(sampleUser.getUuid()).build();
    var sampleCategoryRequest = getSampleCategoryRequestBuilder().build();
    when(userService.findById(currentUserProvider.getCurrentUser()))
        .thenReturn(Optional.of(sampleUser));
    when(categoryRepositoryPort.save(
            Category.builder()
                .name(sampleCategoryRequest.name())
                .userUUID(sampleUser.getUuid())
                .habits(new ArrayList<>())
                .build()))
        .thenReturn(sampleCategory);
    // when
    var actualCategoryResponse = categoryService.createCategory(sampleCategoryRequest);

    var expectedCategoryResponse = getExpectedCategoryResponseBuilder().build();
    // then
    assertThat(actualCategoryResponse).isEqualTo(expectedCategoryResponse);
  }

  @Test
  void deleteTestPositiveCase() {
    // given
    when(categoryRepositoryPort.existsById(currentUserProvider.getCurrentUser())).thenReturn(true);
    var expected = true;
    // when
    var actual = categoryService.delete(currentUserProvider.getCurrentUser());
    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void deleteTestNegativeCase() {
    // given
    when(categoryRepositoryPort.existsById(currentUserProvider.getCurrentUser())).thenReturn(false);
    var expected = false;
    // when
    var actual = categoryService.delete(currentUserProvider.getCurrentUser());
    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void updateCategoryPositiveTestCase() {
    // given
    var sampleCategoryRequest = getSampleUpdateCategoryRequestBuilder().build();
    var expectedCategoryOutput = getExpectedUpdatedCategoryResponseBuilder().build();
    var sampleCategory = getSampleCategoryBuilder(sampleUser.getUuid()).build();
    var sampleUpdatedCategory = getSampleUpdatedCategoryBuilder(sampleCategory.getId()).build();
    when(categoryRepositoryPort.findById(sampleCategory.getId()))
        .thenReturn(Optional.of(sampleCategory));
    when(categoryRepositoryPort.save(sampleCategory)).thenReturn(sampleUpdatedCategory);
    // when
    var actual = categoryService.updateCategory(sampleCategory.getId(), sampleCategoryRequest);
    // then
    assertThat(actual).isEqualTo(expectedCategoryOutput);
  }

  @Test
  void updateCategoryEntityNotFoundException() {
    // given
    var sampleCategoryRequest = getSampleCategoryRequestBuilder().build();
    when(categoryRepositoryPort.findById(currentUserProvider.getCurrentUser()))
        .thenReturn(Optional.empty());
    assertThatThrownBy(
            () -> // when
            categoryService.updateCategory(
                    currentUserProvider.getCurrentUser(), sampleCategoryRequest))
        // then
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
  }

  @Test
  void getAllUserCategoriesPositiveTestCase() {
    // given
    var sampleCategoryForPage =
        getSampleCategoryForPageBuilder(currentUserProvider.getCurrentUser()).build();
    var expectedPageItems =
        List.of(
            CategoryResponse.builder()
                .id(UUID.fromString("f13c542a-9303-4bfd-bddb-ec32de5c0cc5"))
                .name("bar")
                .build());
    var expectedPage = new Page<CategoryResponse>(expectedPageItems);
    when(userService.findById(currentUserProvider.getCurrentUser()))
        .thenReturn(Optional.of(sampleUser));

    when(categoryRepositoryPort.findByUserUUID(sampleUser.getUuid()))
        .thenReturn(List.of(sampleCategoryForPage));
    // when
    var actualPageResponse = categoryService.getAllUserCategories();
    // then
    assertThat(actualPageResponse).isEqualTo(expectedPage);
  }

  @Test
  void getAllUserCategoriesEmptyPage() {
    // given
    when(categoryRepositoryPort.findByUserUUID(currentUserProvider.getCurrentUser())).thenReturn(new ArrayList<>());
    // when
    var actualPageResponse =  categoryService.getAllUserCategories();
    //then
    assertThat(actualPageResponse.getItems().size()).isEqualTo(0);
  }

}
