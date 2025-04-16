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
                .logoUrl(brand.getLogoUrl())
                .origin(brand.getOrigin())
                .website(brand.getWebsite())
                .productCount(brand.getProductCount())
                .status(brand.getStatus().name())
                .build();
    }

    public Brand toEntity(hcmute.lp.backend.model.dto.brand.BrandRequest brandRequest) {
        Brand brand = new Brand();
        brand.setName(brandRequest.getName());
        brand.setDescription(brandRequest.getDescription());
        brand.setLogoUrl(brandRequest.getLogoUrl());
        brand.setOrigin(brandRequest.getOrigin());
        brand.setWebsite(brandRequest.getWebsite());

        if(brandRequest.getStatus() != null) {
            brand.setStatus(Brand.BrandStatus.valueOf(brandRequest.getStatus()));
        } else {
            brand.setStatus(Brand.BrandStatus.ACTIVE);
        }
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
        if (brandRequest.getLogoUrl() != null) {
            brand.setLogoUrl(brandRequest.getLogoUrl());
        }
        if (brandRequest.getOrigin() != null) {
            brand.setOrigin(brandRequest.getOrigin());
        }
        if (brandRequest.getWebsite() != null) {
            brand.setWebsite(brandRequest.getWebsite());
        }
        if(brandRequest.getStatus() != null) {
            brand.setStatus(Brand.BrandStatus.valueOf(brandRequest.getStatus()));
        }

    }
}
