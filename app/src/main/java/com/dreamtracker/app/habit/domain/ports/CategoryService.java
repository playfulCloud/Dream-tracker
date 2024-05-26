package com.dreamtracker.app.habit.domain.ports;

import com.dreamtracker.app.habit.adapters.api.CategoryRequest;
import com.dreamtracker.app.habit.adapters.api.CategoryResponse;
import com.dreamtracker.app.infrastructure.response.Page;

import java.util.UUID;

public interface CategoryService {
   CategoryResponse createCategory(CategoryRequest categoryRequest);
   boolean delete(UUID id);
   CategoryResponse updateCategory(UUID id, CategoryRequest categoryRequest);
   Page<CategoryResponse> getAllUserCategories();
}
