package com.dreamtracker.app.service.impl;

import com.dreamtracker.app.entity.Category;
import com.dreamtracker.app.exception.EntityNotFoundException;
import com.dreamtracker.app.exception.ExceptionMessages;
import com.dreamtracker.app.repository.CategoryRepository;
import com.dreamtracker.app.request.CategoryRequest;
import com.dreamtracker.app.response.CategoryResponse;
import com.dreamtracker.app.response.Page;
import com.dreamtracker.app.security.CurrentUserProvider;
import com.dreamtracker.app.service.CategoryService;
import com.dreamtracker.app.service.UserService;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;
  private final UserService userService;
  private final CurrentUserProvider currentUserProvider;



  @Override
  public CategoryResponse createCategory(CategoryRequest categoryRequest) {
    var ownerOfCategory =
        userService
            .findById(currentUserProvider.getCurrentUser())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    var categoryToCreate =
        Category.builder()
            .name(categoryRequest.name())
            .user(ownerOfCategory)
            .habits(new ArrayList<>())
            .build();

    var categorySavedToDB = categoryRepository.save(categoryToCreate);

    ownerOfCategory.getCategoriesCreatedByUser().add(categorySavedToDB);
    userService.save(ownerOfCategory);

    return mapToResponse(categorySavedToDB);
  }


  @Override
  public boolean delete(UUID id) {
    if (categoryRepository.existsById(id)) {
      categoryRepository.deleteById(id);
      return true;
    }
    return false;
  }


  @Override
  public CategoryResponse updateCategory(UUID id, CategoryRequest categoryRequest) {
    var foundCategory =
        categoryRepository.findById(id)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                       ExceptionMessages.entityNotFoundExceptionMessage
                    ));
    Optional.ofNullable(categoryRequest.name()).ifPresent(foundCategory::setName);
    var categorySaveToDB = categoryRepository.save(foundCategory);
    return mapToResponse(categorySaveToDB);
  }

  @Override
  public Page<CategoryResponse> getAllUserCategories() {
    var ownerOfCategories =
        userService
            .findById(currentUserProvider.getCurrentUser())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                            ExceptionMessages.entityNotFoundExceptionMessage
                    ));
    var listOfCategories = ownerOfCategories.getCategoriesCreatedByUser();
    var listOfCategoryResponses = listOfCategories.stream().map(this::mapToResponse).toList();
    var categoryResponsePage = new Page<CategoryResponse>();
    categoryResponsePage.setItems(listOfCategoryResponses);
    return categoryResponsePage;
  }

  private CategoryResponse mapToResponse(Category category) {
    return CategoryResponse.builder().id(category.getId()).name(category.getName()).build();
  }
}
