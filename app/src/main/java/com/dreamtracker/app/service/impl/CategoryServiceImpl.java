package com.dreamtracker.app.service.impl;

import com.dreamtracker.app.entity.Category;
import com.dreamtracker.app.exception.EntityNotFoundException;
import com.dreamtracker.app.exception.EntitySaveException;
import com.dreamtracker.app.exception.ExceptionMessages;
import com.dreamtracker.app.repository.CategoryRepository;
import com.dreamtracker.app.request.CategoryRequest;
import com.dreamtracker.app.response.CategoryResponse;
import com.dreamtracker.app.response.Page;
import com.dreamtracker.app.security.CurrentUserProvider;
import com.dreamtracker.app.service.CategoryService;
import com.dreamtracker.app.service.UserService;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@Data
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;
  private final UserService userService;
  private final CurrentUserProvider currentUserProvider;

  @Override
  public Optional<Category> findById(UUID id) {
    return categoryRepository.findById(id);
  }

  @Override
  public Optional<Category> save(Category category) {
    return Optional.of(categoryRepository.save(category));
  }

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

    var categorySavedToDB = save(categoryToCreate).orElseThrow(() -> new EntitySaveException(ExceptionMessages.entitySaveExceptionMessage));

    ownerOfCategory.getCategoriesCreatedByUser().add(categorySavedToDB);
    userService.save(ownerOfCategory);

    return mapToResponse(categorySavedToDB);
  }

  @Override
  public void deleteById(UUID id) {
    categoryRepository.deleteById(id);
  }

  @Override
  public boolean delete(UUID id) {
    if (existsById(id)) {
      deleteById(id);
      return true;
    }
    return false;
  }

  @Override
  public boolean existsById(UUID id) {
    return categoryRepository.existsById(id);
  }

  @Override
  public CategoryResponse updateCategory(UUID id, CategoryRequest categoryRequest) {
    var foundCategory =
        findById(id)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                       ExceptionMessages.entityNotFoundExceptionMessage
                    ));
    Optional.ofNullable(categoryRequest.name()).ifPresent(foundCategory::setName);
    var categorySaveToDB = save(foundCategory).orElseThrow(() -> new EntitySaveException(ExceptionMessages.entitySaveExceptionMessage));
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
    Page<CategoryResponse> categoryResponsePage = new Page<>();
    categoryResponsePage.setItems(listOfCategoryResponses);
    return categoryResponsePage;
  }

  private CategoryResponse mapToResponse(Category category) {
    return CategoryResponse.builder().id(category.getId()).name(category.getName()).build();
  }
}
