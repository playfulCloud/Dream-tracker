package com.dreamtracker.app.habit.domain.fixtures;

import com.dreamtracker.app.habit.domain.model.Category;
import com.dreamtracker.app.habit.adapters.api.CategoryRequest;
import com.dreamtracker.app.habit.adapters.api.CategoryResponse;

import java.util.ArrayList;
import java.util.UUID;

public interface CategoryFixtures {

  default Category.CategoryBuilder getSampleCategoryBuilder(UUID userId) {
    return Category.builder().id(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e")).name("foo").userUUID(userId).habits(new ArrayList<>());
  }

  default CategoryRequest.CategoryRequestBuilder getSampleCategoryRequestBuilder(){
    return CategoryRequest.builder().name("foo");
  }

  default CategoryResponse.CategoryResponseBuilder getExpectedCategoryResponseBuilder(){
    return CategoryResponse.builder().id(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e")).name("foo");
  }

  default Category.CategoryBuilder getSampleCategoryForPageBuilder(UUID userId) {
    return Category.builder().id(UUID.fromString("f13c542a-9303-4bfd-bddb-ec32de5c0cc5")).name("bar").userUUID(userId);
  }

  default Category.CategoryBuilder getSampleUpdatedCategoryBuilder(UUID userId) {
    return Category.builder().id(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e")).name("doe").userUUID(userId);
  }

  default CategoryRequest.CategoryRequestBuilder getSampleUpdateCategoryRequestBuilder(){
    return CategoryRequest.builder().name("doe");
  }

  default CategoryResponse.CategoryResponseBuilder getExpectedUpdatedCategoryResponseBuilder(){
    return CategoryResponse.builder().id(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e")).name("doe");
  }


}
