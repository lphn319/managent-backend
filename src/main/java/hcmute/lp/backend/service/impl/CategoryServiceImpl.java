package hcmute.lp.backend.service.impl;

import hcmute.lp.backend.exception.ResourceNotFoundException;
import hcmute.lp.backend.model.dto.category.CategoryDto;
import hcmute.lp.backend.model.dto.category.CategoryRequest;
import hcmute.lp.backend.model.entity.Category;
import hcmute.lp.backend.model.mapper.CategoryMapper;
import hcmute.lp.backend.repository.CategoryRepository;
import hcmute.lp.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;



    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDtoList(categories);
    }

    @Override
    public CategoryDto getCategoryById(int id) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Category not found with id: " + id));
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new IllegalArgumentException("Category already exists with name: " + categoryRequest.getName());
        }
        Category category = categoryMapper.toEntity(categoryRequest);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }


    @Override
    @Transactional
    public CategoryDto updateCategory(int id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Category not found with id: " + id));

        if (!category.getName().equals(categoryRequest.getName()) && categoryRepository.existsByName(categoryRequest.getName())) {
            throw new IllegalArgumentException("Category with this name already exists: " + categoryRequest.getName());
        }

        categoryMapper.updateEntityFromRequest(category, categoryRequest);
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(int id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public boolean existsById(int id) {

        return categoryRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

    @Override
    public CategoryDto updateCategoryStatus(int id, String status) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Category not found with id: " + id));
        category.setStatus(Category.CategoryStatus.valueOf(status));
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    public List<CategoryDto> getCategoriesByParentId(int parentId) {
        return List.of();
    }

    @Override
    public List<CategoryDto> getCategoriesByStatus(String status) {
        return List.of();
    }

    @Override
    public List<CategoryDto> getCategoriesByParentIdAndStatus(int parentId, String status) {
        return List.of();
    }

    @Override
    public Page<CategoryDto> getCategoriesPaginated(int page, int size, String sortBy, String sortDirection, String status) {
        // Tạo đối tượng Pageable với thông số phân trang
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Category> categoryPage;

        // Nếu có tham số status, lọc theo status
        if (status != null && !status.isEmpty()) {
            try {
                Category.CategoryStatus categoryStatus = Category.CategoryStatus.valueOf(status);
                categoryPage = categoryRepository.findByStatus(categoryStatus, pageable);
            } catch (IllegalArgumentException e) {
                // Nếu status không hợp lệ, trả về tất cả
                categoryPage = categoryRepository.findAll(pageable);
            }
        } else {
            // Nếu không có tham số status, trả về tất cả
            categoryPage = categoryRepository.findAll(pageable);
        }

        // Chuyển đổi Page<Category> thành Page<CategoryDto>
        return categoryPage.map(categoryMapper::toDto);
    }

}