package com.dreamtracker.app.service;

import com.dreamtracker.app.entity.Category;
import com.dreamtracker.app.request.CategoryRequest;
import com.dreamtracker.app.response.CategoryResponse;
import com.dreamtracker.app.response.Page;

import java.util.Optional;
import java.util.UUID;

public interface CategoryService {
   CategoryResponse createCategory(CategoryRequest categoryRequest);
   boolean delete(UUID id);
   CategoryResponse updateCategory(UUID id, CategoryRequest categoryRequest);
   Page<CategoryResponse> getAllUserCategories();
}
