package hcmute.lp.backend.service;

import hcmute.lp.backend.model.dto.category.CategoryDto;
import hcmute.lp.backend.model.dto.category.CategoryRequest;


import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(int id);
    CategoryDto createCategory(CategoryRequest categoryRequest);
    CategoryDto updateCategory(int id, CategoryRequest categoryRequest);
    void deleteCategory(int id);
    boolean existsById(int id);
    boolean existsByName(String name);
}