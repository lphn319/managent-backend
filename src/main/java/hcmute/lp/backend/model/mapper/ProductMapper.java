package hcmute.lp.backend.model.mapper;

import hcmute.lp.backend.model.dto.product.ProductDto;
import hcmute.lp.backend.model.dto.product.ProductRequest;
import hcmute.lp.backend.model.entity.Product;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "categories", source = "categories")
    @Mapping(target = "brand", source = "brand")
    ProductDto toDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    Product toEntity(ProductRequest productRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "description", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "price", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "imageUrl", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "quantity", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    void updateEntityFromRequest(@MappingTarget Product product, ProductRequest productRequest);

    List<ProductDto> toDtoList(List<Product> products);
}
