package hcmute.lp.backend.model.mapper;

import hcmute.lp.backend.model.dto.discount.DiscountDto;
import hcmute.lp.backend.model.dto.discount.DiscountRequest;
import hcmute.lp.backend.model.entity.Discount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiscountMapper {
    DiscountMapper DISCOUNT_MAPPER = Mappers.getMapper(DiscountMapper.class);

    @Mapping(source = "discount.id", target = "id")
    @Mapping(source = "discount.code", target = "code")
    @Mapping(source = "discount.name", target = "name")
    @Mapping(source = "discount.description", target = "description")
    @Mapping(source = "discount.discountRate", target = "discountRate")
    @Mapping(source = "discount.startDate", target = "startDate")
    @Mapping(source = "discount.endDate", target = "endDate")
    @Mapping(source = "discount.active", target = "isActive")
    DiscountDto toDto(Discount discount);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    Discount toEntity(DiscountRequest discountRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    void updateEntityFromRequest(@MappingTarget Discount discount, DiscountRequest discountRequest);

    List<DiscountDto> toDtoList(List<Discount> discounts);
}