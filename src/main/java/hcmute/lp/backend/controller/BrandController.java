package hcmute.lp.backend.controller;

import hcmute.lp.backend.model.common.ApiResponse;
import hcmute.lp.backend.model.dto.brand.BrandDto;
import hcmute.lp.backend.model.dto.brand.BrandRequest;
import hcmute.lp.backend.service.BrandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
@Tag(name = "Brand API")
public class BrandController {
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    // Get all brands
    @GetMapping
    public ResponseEntity<ApiResponse<List<BrandDto>>> getAllBrands() {
        List<BrandDto> brands = brandService.getAllBrands();
        return ResponseEntity.ok(ApiResponse.success("Brands retrieved successfully", brands));
    }

    // Get brand by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandDto>> getBrandById(@PathVariable int id) {
        BrandDto brand = brandService.getBrandById(id);
        return ResponseEntity.ok(ApiResponse.success("Brand found", brand));
    }

    // Create a new brand
    @PostMapping
    public ResponseEntity<ApiResponse<BrandDto>> createBrand(@Valid @RequestBody BrandRequest brandRequest) {
        BrandDto createdBrand = brandService.createBrand(brandRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Brand created", createdBrand));
    }

    // Update an existing brand
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandDto>> updateBrand(@PathVariable int id, @Valid @RequestBody BrandRequest brandRequest) {
        BrandDto updatedBrand = brandService.updateBrand(id, brandRequest);
        return ResponseEntity.ok(ApiResponse.success("Brand updated successfully", updatedBrand));
    }

    // Delete a brand
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBrand(@PathVariable int id) {
        brandService.deleteBrand(id);
        return ResponseEntity.ok(ApiResponse.success("Brand deleted successfully", null));
    }

}