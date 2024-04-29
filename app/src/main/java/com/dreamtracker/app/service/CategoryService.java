package com.dreamtracker.app.service;

import com.dreamtracker.app.entity.Category;
import com.dreamtracker.app.request.CategoryRequest;
import com.dreamtracker.app.response.CategoryResponse;
import com.dreamtracker.app.response.Page;

import java.util.Optional;
import java.util.UUID;

public interface CategoryService {
   Optional<Category>save(Category category);
   CategoryResponse createCategory(CategoryRequest categoryRequest);
   void deleteById(UUID id);
   boolean delete(UUID id);
   boolean existsById(UUID id);
   CategoryResponse updateCategory(UUID id, CategoryRequest categoryRequest);
   Page<CategoryResponse> getAllUserCategories();
}
