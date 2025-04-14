package hcmute.lp.backend.service;

import hcmute.lp.backend.model.dto.brand.BrandDto;
import hcmute.lp.backend.model.dto.brand.BrandRequest;

import java.util.List;

public interface BrandService {
    List<BrandDto> getAllBrands();
    BrandDto getBrandById(int id);
    BrandDto createBrand(BrandRequest brandRequest);
    List<BrandDto> createBrands(List<BrandRequest> brandRequests);
    BrandDto updateBrand(int id, BrandRequest brandRequest);
    void deleteBrand(int id);
    boolean existsById(int id);
    boolean existsByName(String name);
}