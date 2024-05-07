package com.dreamtracker.app.fixtures;

import com.dreamtracker.app.entity.Category;
import com.dreamtracker.app.entity.User;
import com.dreamtracker.app.request.CategoryRequest;
import com.dreamtracker.app.response.CategoryResponse;

import java.util.UUID;

public interface CategoryFixtures {

  default Category.CategoryBuilder getSampleCategoryBuilder(User user) {
    return Category.builder().id(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e")).name("foo").user(user);
  }

  default CategoryRequest.CategoryRequestBuilder getSampleCategoryRequestBuilder(){
    return CategoryRequest.builder().name("foo");
  }

  default CategoryResponse.CategoryResponseBuilder getExpectedCategoryResponseBuilder(){
    return CategoryResponse.builder().id(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e")).name("foo");
  }
}
