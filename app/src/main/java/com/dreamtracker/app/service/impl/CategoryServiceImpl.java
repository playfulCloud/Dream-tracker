package com.dreamtracker.app.service.impl;

import com.dreamtracker.app.entity.Category;
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
    public Optional<Category> save(Category category) {
        return Optional.of(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        var ownerOfCategory = userService.findById(currentUserProvider.getCurrentUser()).orElseThrow(() -> new RuntimeException("User not found"));

        var categoryToCreate = Category.builder()
                .name(categoryRequest.name())
                .user(ownerOfCategory)
                .habits(new ArrayList<>())
                .build();

        var categorySavedToDB = save(categoryToCreate).orElseThrow(() -> new RuntimeException("Error saving to database"));

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
       if(existsById(id)){
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
        return null;
    }

    @Override
    public Page<CategoryResponse> getAllUserCategories() {
        return null;
    }


    private CategoryResponse mapToResponse(Category category){
        return CategoryResponse.builder()
                .name(category.getName())
                .build();
    }
}
