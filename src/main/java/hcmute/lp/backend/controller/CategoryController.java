package hcmute.lp.backend.controller;

import hcmute.lp.backend.model.entity.Category;
import hcmute.lp.backend.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
     private final CategoryService categoryService;

     public CategoryController(CategoryService categoryService) {
         this.categoryService = categoryService;
     }

     @GetMapping
     public ResponseEntity<List<Category>> getCategories() {
         List<Category> categories = categoryService.getCategories();
         return ResponseEntity.ok(categories);
     }
}
