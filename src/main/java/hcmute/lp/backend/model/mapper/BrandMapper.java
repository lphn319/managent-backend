package hcmute.lp.backend.model.mapper;

import hcmute.lp.backend.model.dto.brand.BrandDto;
import hcmute.lp.backend.model.entity.Brand;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BrandMapper {
    public BrandDto toDto(Brand brand) {
        return BrandDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .description(brand.getDescription())
                .build();
    }

    public Brand toEntity(hcmute.lp.backend.model.dto.brand.BrandRequest brandRequest) {
        Brand brand = new Brand();
        brand.setName(brandRequest.getName());
        brand.setDescription(brandRequest.getDescription());
        return brand;
    }

    public List<BrandDto> toDtoList(List<Brand> brands) {
        return brands.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void updateEntityFromRequest(Brand brand, hcmute.lp.backend.model.dto.brand.BrandRequest brandRequest) {
        if (brandRequest.getName() != null) {
            brand.setName(brandRequest.getName());
        }
        if (brandRequest.getDescription() != null) {
            brand.setDescription(brandRequest.getDescription());
        }
    }
}
