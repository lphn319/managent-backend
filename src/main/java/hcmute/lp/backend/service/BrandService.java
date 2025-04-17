package hcmute.lp.backend.service;

import hcmute.lp.backend.model.dto.brand.BrandDto;
import hcmute.lp.backend.model.dto.brand.BrandRequest;
import hcmute.lp.backend.model.entity.Brand;

import java.util.List;
import java.util.Map;

public interface BrandService {
    List<BrandDto> getAllBrands();
    BrandDto getBrandById(int id);
    BrandDto createBrand(BrandRequest brandRequest);
    List<BrandDto> createBrands(List<BrandRequest> brandRequests);
    BrandDto updateBrand(int id, BrandRequest brandRequest);
    void deleteBrand(int id);
    boolean existsById(int id);
    boolean existsByName(String name);
    BrandDto updateBrandStatus(int id, String status);
    List<BrandDto> getFeaturedBrands(int limit);

    Map<String, Integer> getBrandStatistics();
}