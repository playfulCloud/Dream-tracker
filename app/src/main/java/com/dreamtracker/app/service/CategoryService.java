package com.dreamtracker.app.service;

import com.dreamtracker.app.domain.request.CategoryRequest;
import com.dreamtracker.app.domain.response.CategoryResponse;
import com.dreamtracker.app.domain.response.Page;

import java.util.UUID;

public interface CategoryService {
   CategoryResponse createCategory(CategoryRequest categoryRequest);
   boolean delete(UUID id);
   CategoryResponse updateCategory(UUID id, CategoryRequest categoryRequest);
   Page<CategoryResponse> getAllUserCategories();
}
