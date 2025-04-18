package hcmute.lp.backend.model.mapper;

import hcmute.lp.backend.model.dto.category.CategoryDto;
import hcmute.lp.backend.model.dto.category.CategoryRequest;
import hcmute.lp.backend.model.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    public CategoryDto toDto(Category category) {
        CategoryDto dto = CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .productCount(category.getProductCount())
                .status(category.getStatus().name())
                .build();

        if (category.getParent() != null) {
            dto.setParentId(category.getParent().getId());
        }

        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            dto.setChildren(toDtoList(category.getChildren()));
        }

        return dto;
    }

    public List<CategoryDto> toDtoList(List<Category> categories) {
        return categories.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Category toEntity(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        if (categoryRequest.getParentId() != null) {
            Category parent = new Category();
            parent.setId(categoryRequest.getParentId());
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        if (categoryRequest.getStatus() != null) {
            category.setStatus(Category.CategoryStatus.valueOf(categoryRequest.getStatus()));
        } else {
            category.setStatus(Category.CategoryStatus.ACTIVE);
        }

        return category;
    }

    public void updateEntityFromRequest(Category category, CategoryRequest categoryRequest) {
        if (categoryRequest.getName() != null) {
            category.setName(categoryRequest.getName());
        }
        if (categoryRequest.getDescription() != null) {
            category.setDescription(categoryRequest.getDescription());
        }
        if (categoryRequest.getStatus() != null) {
            category.setStatus(Category.CategoryStatus.valueOf(categoryRequest.getStatus()));
        }
    }
}
