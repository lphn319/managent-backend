package hcmute.lp.backend.controller;

import hcmute.lp.backend.model.common.ApiResponse;
import hcmute.lp.backend.model.dto.category.CategoryDto;
import hcmute.lp.backend.model.dto.category.CategoryRequest;
import hcmute.lp.backend.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Category API")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // GET ALL CATEGORIES
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success("Categories retrieved successfully", categories));
    }

    // GET CATEGORY BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> getCategoryById(@PathVariable int id) {
        CategoryDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success("Category found", category));
    }

    // CREATE CATEGORY
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDto>> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryDto createdCategory = categoryService.createCategory(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Category created", createdCategory));
    }

    // UPDATE CATEGORY
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> updateCategory(@PathVariable int id, @Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryDto updatedCategory = categoryService.updateCategory(id, categoryRequest);
        return ResponseEntity.ok(ApiResponse.success("Category updated successfully", updatedCategory));
    }

    // DELETE CATEGORY
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully", null));
    }
}