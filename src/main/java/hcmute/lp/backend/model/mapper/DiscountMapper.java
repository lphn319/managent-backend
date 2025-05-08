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


    DiscountDto toDto(Discount discount);

    Discount toEntity(DiscountRequest discountRequest);

    void updateEntityFromRequest(@MappingTarget Discount discount, DiscountRequest discountRequest);

    List<DiscountDto> toDtoList(List<Discount> discounts);
}