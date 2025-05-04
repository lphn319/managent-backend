package hcmute.lp.backend.model.mapper;

import hcmute.lp.backend.model.dto.supplier.SupplierDto;
import hcmute.lp.backend.model.dto.supplier.SupplierRequest;
import hcmute.lp.backend.model.entity.Category;
import hcmute.lp.backend.model.entity.Supplier;
import hcmute.lp.backend.repository.CategoryRepository;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CategoryMapper.class})
public abstract class SupplierMapper {
    @Autowired
    protected CategoryRepository categoryRepository;

    public abstract SupplierDto toDto(Supplier supplier);

    public abstract List<SupplierDto> toDtoList(List<Supplier> suppliers);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "imports", ignore = true)
    @Mapping(target = "categories", expression = "java(mapCategories(supplierRequest.getCategoryIds()))")
    public abstract Supplier toEntity(SupplierRequest supplierRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "imports", ignore = true)
    @Mapping(target = "companyName", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "address", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "phone", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "email", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "description", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "logo", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "status", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "categories", expression = "java(supplierRequest.getCategoryIds() != null ? mapCategories(supplierRequest.getCategoryIds()) : supplier.getCategories())")
    public abstract void updateEntityFromRequest(@MappingTarget Supplier supplier, SupplierRequest supplierRequest);

    // Helper method để map từ danh sách id của category sang danh sách entity Category
    protected Set<Category> mapCategories(Set<Integer> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return new HashSet<>();
        }
        return categoryIds.stream()
                .map(id -> categoryRepository.findById(id).orElse(null))
                .filter(category -> category != null)
                .collect(Collectors.toSet());
    }
}