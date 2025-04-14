package hcmute.lp.backend.service;

import hcmute.lp.backend.exception.ResourceNotFoundException;
import hcmute.lp.backend.model.dto.category.CategoryDto;
import hcmute.lp.backend.model.dto.category.CategoryRequest;
import hcmute.lp.backend.model.entity.Category;
import hcmute.lp.backend.model.mapper.CategoryMapper;
import hcmute.lp.backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

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
}