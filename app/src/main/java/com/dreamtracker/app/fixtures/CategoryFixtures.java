package com.dreamtracker.app.fixtures;

import com.dreamtracker.app.entity.Category;
import com.dreamtracker.app.request.CategoryRequest;
import com.dreamtracker.app.response.CategoryResponse;
import java.util.UUID;

public interface CategoryFixtures {

  default Category.CategoryBuilder getSampleCategoryBuilder(UUID userId) {
    return Category.builder().id(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e")).name("foo").userUUID(userId);
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
}
