package com.dreamtracker.app.controller;


import com.dreamtracker.app.request.CategoryRequest;
import com.dreamtracker.app.response.CategoryResponse;
import com.dreamtracker.app.response.Page;
import com.dreamtracker.app.service.CategoryService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1")
@Data
public class CategoryController {


    private final CategoryService categoryService;


    @PostMapping("/category")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest){
       return new ResponseEntity<>(categoryService.createCategory(categoryRequest), HttpStatus.CREATED);
    }


    @PutMapping("/category/{category-id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable("category-id") UUID id ,@RequestBody CategoryRequest categoryRequest){
       return new ResponseEntity<>(categoryService.updateCategory(id,categoryRequest),HttpStatus.OK);
    }


    @GetMapping("/category")
    public ResponseEntity<Page<CategoryResponse>> getAllCategoriesCreatedByUser(){
        return new ResponseEntity<>(categoryService.getAllUserCategories(),HttpStatus.OK);
    }


    @DeleteMapping("/category/{category-id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("category-id") UUID id){
        return categoryService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

}