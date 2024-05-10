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
import com.dreamtracker.app.response.CategoryResponse;
import com.dreamtracker.app.response.Page;
import com.dreamtracker.app.security.CurrentUserProvider;
import com.dreamtracker.app.service.CategoryService;
import com.dreamtracker.app.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
  void createCategoryPositiveTestCase() {
    // given
    var sampleCategory = getSampleCategoryBuilder(sampleUser.getUuid()).build();
    var sampleCategoryRequest = getSampleCategoryRequestBuilder().build();
    when(userService.findById(currentUserProvider.getCurrentUser()))
        .thenReturn(Optional.of(sampleUser));
    when(categoryRepository.save(
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
  void createCategoryEntityNotFoundExceptionTest() {
    // given
    var sampleCategoryRequest = getSampleCategoryRequestBuilder().build();
    when(userService.findById(currentUserProvider.getCurrentUser())).thenReturn(Optional.empty());
    assertThatThrownBy(
            () -> { // when
              categoryService.createCategory(sampleCategoryRequest);
            }) // then
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
  }

  @Test
  void deleteTestPositiveCase() {
    // given
    when(categoryRepository.existsById(currentUserProvider.getCurrentUser())).thenReturn(true);
    var expected = true;
    // when
    var actual = categoryService.delete(currentUserProvider.getCurrentUser());
    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void deleteTestNegativeCase() {
    // given
    when(categoryRepository.existsById(currentUserProvider.getCurrentUser())).thenReturn(false);
    var expected = false;
    // when
    var actual = categoryService.delete(currentUserProvider.getCurrentUser());
    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void updateCategoryPositiveTestCase() {
    // given
    var sampleCategoryRequest = getSampleCategoryRequestBuilder().build();
    var expectedCategoryOutput = getExpectedCategoryResponseBuilder().build();
    var sampleCategory = getSampleCategoryBuilder(sampleUser.getUuid()).build();
    when(categoryRepository.findById(sampleCategory.getId()))
        .thenReturn(Optional.of(sampleCategory));
    when(categoryRepository.save(sampleCategory)).thenReturn(sampleCategory);
    // when
    var actual = categoryService.updateCategory(sampleCategory.getId(), sampleCategoryRequest);
    // then
    assertThat(actual).isEqualTo(expectedCategoryOutput);
  }

  @Test
  void updateCategoryEntityNotFoundException() {
    // given
    var sampleCategoryRequest = getSampleCategoryRequestBuilder().build();
    when(categoryRepository.findById(currentUserProvider.getCurrentUser()))
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
    var sampleUser = getSampleUserWithCategories(currentUserProvider.getCurrentUser()).build();
    var expectedPage = new Page<CategoryResponse>();
    var expectedPageItems =
        List.of(
            CategoryResponse.builder()
                .id(UUID.fromString("f13c542a-9303-4bfd-bddb-ec32de5c0cc5"))
                .name("bar")
                .build());
    expectedPage.setItems(expectedPageItems);
    when(userService.findById(currentUserProvider.getCurrentUser()))
        .thenReturn(Optional.of(sampleUser));

    when(categoryRepository.findByUserUUID(sampleUser.getUuid()))
        .thenReturn(List.of(sampleCategoryForPage));
    // when
    var actualPageResponse = categoryService.getAllUserCategories();
    // then
    assertThat(actualPageResponse).isEqualTo(expectedPage);
  }

  @Test
  void getAllUserCategoriesEmptyPage() {
    // given
    when(userService.findById(currentUserProvider.getCurrentUser())).thenReturn(Optional.empty());
    // when
    var actualPageResponse =  categoryService.getAllUserCategories();
    assertThat(actualPageResponse.getItems().size()).isEqualTo(0);
  }
}
