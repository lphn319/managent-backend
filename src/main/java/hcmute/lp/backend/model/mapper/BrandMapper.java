package hcmute.lp.backend.model.mapper;

import hcmute.lp.backend.model.dto.brand.BrandDto;
import hcmute.lp.backend.model.dto.brand.BrandRequest;
import hcmute.lp.backend.model.entity.Brand;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BrandMapper {

    BrandMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(BrandMapper.class);

    @Mapping(target = "productCount", ignore = true)
    @Mapping(target = "status", expression = "java(brandRequest.getStatus() != null ? Brand.BrandStatus.valueOf(brandRequest.getStatus()) : Brand.BrandStatus.ACTIVE)")
    Brand toEntity(BrandRequest brandRequest);

    @Mapping(target = "status", expression = "java(brand.getStatus().name())")
    BrandDto toDto(Brand brand);

    List<BrandDto> toDtoList(List<Brand> brands);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productCount", ignore = true)
    @Mapping(target = "status", expression = "java(brandRequest.getStatus() != null ? Brand.BrandStatus.valueOf(brandRequest.getStatus()) : brand.getStatus())")
    void updateEntityFromRequest(BrandRequest brandRequest, @MappingTarget Brand brand);
}