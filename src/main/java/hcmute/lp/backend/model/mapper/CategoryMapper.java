package hcmute.lp.backend.model.mapper;

import hcmute.lp.backend.model.dto.category.CategoryDto;
import hcmute.lp.backend.model.dto.category.CategoryRequest;
import hcmute.lp.backend.model.entity.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    CategoryMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(CategoryMapper.class);

    @Mapping(target = "children", ignore = true)
    @Mapping(target = "parent", source = "parentId", qualifiedByName = "parentIdToParent")
    Category toEntity(CategoryRequest categoryRequest);

    @Mapping(target = "parentId", source = "parent.id")
    CategoryDto toDto(Category category);

    List<CategoryDto> toDtoList(List<Category> categories);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "productCount", ignore = true)
    @Mapping(target = "parent", source = "parentId", qualifiedByName = "parentIdToParent")
    void updateEntityFromRequest(CategoryRequest categoryRequest, @MappingTarget Category category);

    @Named("parentIdToParent")
    default Category parentIdToParent(Integer parentId) {
        if (parentId == null) {
            return null;
        }
        Category parent = new Category();
        parent.setId(parentId);
        return parent;
    }
}