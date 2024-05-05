package com.dreamtracker.app.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dreamtracker.app.entity.Category;
import com.dreamtracker.app.entity.User;
import com.dreamtracker.app.exception.EntityNotFoundException;
import com.dreamtracker.app.exception.EntitySaveException;
import com.dreamtracker.app.exception.ExceptionMessages;
import com.dreamtracker.app.repository.CategoryRepository;
import com.dreamtracker.app.request.CategoryRequest;
import com.dreamtracker.app.response.CategoryResponse;
import com.dreamtracker.app.security.CurrentUserProvider;
import com.dreamtracker.app.service.CategoryService;
import com.dreamtracker.app.service.UserService;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class CategoryServiceImplTest {

  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();

  @BeforeEach
  void setUp() {
  }

  @Test
  void findById() {}

  @Test
  void save() {}

  @Test
  void createCategoryWithSuccess() {
    CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
    UserService userService = Mockito.mock(UserService.class);
    CategoryService categoryService =
        new CategoryServiceImpl(categoryRepository, userService, currentUserProvider);
    var sampleUser = createSampleUserForTest();
    when(userService.findById(currentUserProvider.getCurrentUser())).thenReturn(sampleUser);
    when(categoryRepository.save(any())).thenReturn(createSampleCategory(sampleUser.get()));

    CategoryResponse actualCategoryResponse =
        categoryService.createCategory(createCategoryRequest());

    assertThat(actualCategoryResponse.name()).isEqualTo("foo");
    verify(categoryRepository).save(any(Category.class));
  }

  @Test
  void createCategoryEntityNotFoundExceptionTest() {
      CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
      UserService userService = Mockito.mock(UserService.class);
      CategoryService categoryService =
              new CategoryServiceImpl(categoryRepository, userService, currentUserProvider);
      when(userService.findById(currentUserProvider.getCurrentUser())).thenReturn(Optional.empty());
      when(categoryRepository.save(any())).thenReturn(Optional.empty());
      assertThatThrownBy(() -> {
          categoryService.createCategory(createCategoryRequest());
      }).isInstanceOf(EntityNotFoundException.class).hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
  }

    @Test
    void createCategoryEntitySaveExceptionTest() {
        CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
        UserService userService = Mockito.mock(UserService.class);
        var sampleUser = createSampleUserForTest();
        CategoryService categoryService =
                new CategoryServiceImpl(categoryRepository, userService, currentUserProvider);
        when(categoryRepository.save(any())).thenReturn(createSampleCategory(sampleUser.get()));
        assertThatThrownBy(() -> {
            categoryService.createCategory(createCategoryRequest());
        }).isInstanceOf(EntitySaveException.class).hasMessage(ExceptionMessages.entitySaveExceptionMessage);
    }



  @Test
  void deleteById() {}

  @Test
  void delete() {}

  @Test
  void existsById() {}

  @Test
  void updateCategory() {}

  @Test
  void getAllUserCategories() {}

  @Test
  void getCategoryRepository() {}

  private Optional<User> createSampleUserForTest() {
    var user =
        User.builder()
            .uuid(currentUserProvider.getCurrentUser())
            .name("John")
            .surname("Doe")
            .categoriesCreatedByUser(new ArrayList<>())
            .goals(new ArrayList<>())
            .habits(new ArrayList<>())
            .build();

    return Optional.of(user);
  }

  private CategoryRequest createCategoryRequest() {
    return CategoryRequest.builder().name("foo").build();
  }

  private CategoryResponse createExpectedCategoryOutput() {
    return CategoryResponse.builder().name("foo").build();
  }

  private Category createSampleCategory(User owner) {
    return Category.builder().name("foo").user(owner).build();
  }
}
