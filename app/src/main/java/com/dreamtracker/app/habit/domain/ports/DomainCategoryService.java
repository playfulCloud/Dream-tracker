package com.dreamtracker.app.habit.domain.ports;

import com.dreamtracker.app.habit.domain.model.Category;
import com.dreamtracker.app.habit.domain.model.Habit;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.habit.adapters.api.CategoryRequest;
import com.dreamtracker.app.habit.adapters.api.CategoryResponse;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.domain.ports.UserService;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class DomainCategoryService implements CategoryService {

  private final CategoryRepositoryPort categoryRepositoryPort;
  private final UserService userService;
  private final CurrentUserProvider currentUserProvider;
  private final HabitRepositoryPort habitREpo;

  @Override
  public CategoryResponse createCategory(CategoryRequest categoryRequest) {

    var categoryToCreate =
        Category.builder()
            .name(categoryRequest.name())
            .userUUID(currentUserProvider.getCurrentUser())
            .habits(new ArrayList<>())
            .build();

    var categorySavedToDB = categoryRepositoryPort.save(categoryToCreate);
    return mapToResponse(categorySavedToDB);
  }

  @Override
  @Transactional
  public boolean delete(UUID id) {
    var category = categoryRepositoryPort.findById(id).orElseThrow(() -> new EntityNotFoundException("Tesstestestesjaklldfjasjfkdsaljfkdsjakkl"));
    var habits = category.getHabits();

    var habitToRemove = new ArrayList<Habit>();

    for(var habit : habits){
      if(habit.getCategories().size() == 1){
        habitToRemove.add(habit);
      }
    }
    for (Habit habit : habitToRemove) {
      habit.getCategories().remove(category);
      habitREpo.save(habit);
    }

    categoryRepositoryPort.deleteById(id);


    return true;
  }

  @Override
  public CategoryResponse updateCategory(UUID id, CategoryRequest categoryRequest) {
    var foundCategory =
        categoryRepositoryPort
            .findById(id)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    Optional.ofNullable(categoryRequest.name()).ifPresent(foundCategory::setName);
    var categorySaveToDB = categoryRepositoryPort.save(foundCategory);
    return mapToResponse(categorySaveToDB);
  }

  @Override
  public Page<CategoryResponse> getAllUserCategories() {
    var listOfCategories = categoryRepositoryPort.findByUserUUID(currentUserProvider.getCurrentUser());
    var listOfCategoryResponses = listOfCategories.stream().map(this::mapToResponse).toList();
    var categoryResponsePage = new Page<CategoryResponse>(listOfCategoryResponses);
    return categoryResponsePage;
  }

  private CategoryResponse mapToResponse(Category category) {
    return CategoryResponse.builder().id(category.getId()).name(category.getName()).build();
  }
}
