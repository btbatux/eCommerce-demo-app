package com.btbatux.dream_shops.controller;

import com.btbatux.dream_shops.exception.AlreadyExistsException;
import com.btbatux.dream_shops.model.Category;
import com.btbatux.dream_shops.response.ApiResponse;
import com.btbatux.dream_shops.service.category.CategoryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            return ResponseEntity.
                    ok(new ApiResponse("Found", categoryService.getAllCategories()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(new ApiResponse("Error", e.getMessage()));
        }

    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
        try {
            Category newCategory = categoryService.addCategory(category);
            return ResponseEntity.ok(new ApiResponse("Category added successfully", newCategory));
        } catch (AlreadyExistsException e) {
            // Eğer kategori zaten varsa 409 Conflict dönülür.
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            // Diğer hatalar için 500 Internal Server Error döndürülür.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("An error occurred: " + e.getMessage(), null));
        }
    }


    @GetMapping("/category/{categoryId}/category")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable("categoryId") Long id) {
        try {
            return ResponseEntity.
                    ok(new ApiResponse("Found", categoryService.getCategoryById(id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).
                    body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/category{categoryName}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable("categoryName") String name) {
        try {
            return ResponseEntity.
                    ok(new ApiResponse("Found", categoryService.getCategoryByName(name)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).
                    body(new ApiResponse(e.getMessage(), null));
        }
    }


    @DeleteMapping("/category/{categoryId}/delete")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable("categoryId") Long id) {
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Deleted", null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).
                    body(new ApiResponse(e.getMessage(), null));
        }
    }


    @PutMapping("/category/{categoryId}/update")
    public ResponseEntity<ApiResponse> updateCategoryById(@PathVariable("categoryId") Long id, @RequestBody Category category) {
        try {
            return ResponseEntity.
                    ok(new ApiResponse("Update", categoryService.updateCategory(category, id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).
                    body(new ApiResponse(e.getMessage(), null));
        }
    }

}
