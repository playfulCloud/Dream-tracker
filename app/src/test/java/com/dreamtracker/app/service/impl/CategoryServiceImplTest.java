package com.dreamtracker.app.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.dreamtracker.app.entity.Category;
import com.dreamtracker.app.entity.User;
import com.dreamtracker.app.exception.EntityNotFoundException;
import com.dreamtracker.app.exception.ExceptionMessages;
import com.dreamtracker.app.fixtures.CategoryFixtures;
import com.dreamtracker.app.fixtures.UserFixtures;
import com.dreamtracker.app.repository.CategoryRepository;
import com.dreamtracker.app.security.CurrentUserProvider;
import com.dreamtracker.app.service.CategoryService;
import com.dreamtracker.app.service.UserService;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CategoryServiceImplTest implements CategoryFixtures, UserFixtures {

  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
  private final CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
  private final UserService userService = Mockito.mock(UserService.class);
  private User sampleUser;
  private CategoryService categoryService;

  @BeforeEach
  void setUp() {
    sampleUser = getSampleUser(currentUserProvider.getCurrentUser()).build();
    categoryService = new CategoryServiceImpl(categoryRepository, userService, currentUserProvider);
  }

  @Test
  void createCategoryWithSuccess() {
    var sampleCategory = getSampleCategoryBuilder(sampleUser).build();
    var sampleCategoryRequest = getSampleCategoryRequestBuilder().build();
    when(userService.findById(currentUserProvider.getCurrentUser()))
        .thenReturn(Optional.of(sampleUser));
    when(categoryRepository.save(
            Category.builder()
                .name(sampleCategoryRequest.name())
                .user(sampleUser)
                .habits(new ArrayList<>())
                .build()))
        .thenReturn(sampleCategory);
    var actualCategoryResponse = categoryService.createCategory(sampleCategoryRequest);

    var expectedCategoryResponse = getExpectedCategoryResponseBuilder().build();
    assertThat(actualCategoryResponse).isEqualTo(expectedCategoryResponse);
  }

  @Test
  void createCategoryEntityNotFoundExceptionTest() {
    var sampleCategoryRequest = getSampleCategoryRequestBuilder().build();
    when(userService.findById(currentUserProvider.getCurrentUser())).thenReturn(Optional.empty());
    assertThatThrownBy(
            () -> {
              categoryService.createCategory(sampleCategoryRequest);
            })
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
  }

  @Test
  void deleteTestPositiveCase() {
    when(categoryRepository.existsById(currentUserProvider.getCurrentUser())).thenReturn(true);
    var expected = true;
    var actual = categoryService.delete(currentUserProvider.getCurrentUser());
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void deleteTestNegativeCase() {
    when(categoryRepository.existsById(currentUserProvider.getCurrentUser())).thenReturn(false);
    var expected = false;
    var actual = categoryService.delete(currentUserProvider.getCurrentUser());
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void updateCategoryPositiveTestCase() {
    var sampleCategoryRequest = getSampleCategoryRequestBuilder().build();
    var expectedCategoryOutput = getExpectedCategoryResponseBuilder().build();
    var sampleCategory = getSampleCategoryBuilder(sampleUser).build();
    when(categoryRepository.findById(sampleCategory.getId()))
        .thenReturn(Optional.of(sampleCategory));
    when(categoryRepository.save(sampleCategory)).thenReturn(sampleCategory);

    var actual = categoryService.updateCategory(sampleCategory.getId(), sampleCategoryRequest);
    assertThat(actual).isEqualTo(expectedCategoryOutput);
  }

  @Test
  void updateCategoryEntityNotFoundException() {
    var sampleCategoryRequest = getSampleCategoryRequestBuilder().build();
    var expectedCategoryOutput = getExpectedCategoryResponseBuilder().build();
    when(categoryRepository.findById(currentUserProvider.getCurrentUser()))
        .thenReturn(Optional.empty());
    assertThatThrownBy(
            () ->
                categoryService.updateCategory(
                    currentUserProvider.getCurrentUser(), sampleCategoryRequest))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
  }

  @Test
  void getAllUserCategories() {}
}
