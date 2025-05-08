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


    ProductDto toDto(Product product);

    Product toEntity(ProductRequest productRequest);


    void updateEntityFromRequest(@MappingTarget Product product, ProductRequest productRequest);

    List<ProductDto> toDtoList(List<Product> products);
}
