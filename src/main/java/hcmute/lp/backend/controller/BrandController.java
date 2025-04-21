package hcmute.lp.backend.controller;

import hcmute.lp.backend.model.common.ApiResponse;
import hcmute.lp.backend.model.dto.brand.BrandDto;
import hcmute.lp.backend.model.dto.brand.BrandRequest;
import hcmute.lp.backend.service.BrandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/brands")
@Tag(name = "Brand API")
@CrossOrigin(origins = "http://localhost:4200")
public class BrandController {
    @Autowired
    private BrandService brandService;

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

    @PatchMapping("/{id}/status")
    public ResponseEntity<BrandDto> updateBrandStatus(
            @PathVariable int id,
            @RequestBody Map<String, String> statusRequest) {
        String status = statusRequest.get("status");
        BrandDto updatedBrand = brandService.updateBrandStatus(id, status);
        return ResponseEntity.ok(updatedBrand);
    }

    @GetMapping("/featured")
    public ResponseEntity<List<BrandDto>> getFeaturedBrands(
            @RequestParam(defaultValue = "5") int limit) {
        List<BrandDto> featuredBrands = brandService.getFeaturedBrands(limit);
        return ResponseEntity.ok(featuredBrands);
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Integer>> getBrandStatistics() {
        Map<String, Integer> statistics = brandService.getBrandStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/pagination")
    public ResponseEntity<ApiResponse<Page<BrandDto>>> getBrandsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(required = false) String status) {
        Page<BrandDto> brandPage = brandService.getBrandsPaginated(
                page, size, sortBy, sortDirection, status);
        return ResponseEntity.ok(ApiResponse.success("Brands retrieved successfully", brandPage));
    }
}