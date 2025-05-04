package hcmute.lp.backend.controller;

import hcmute.lp.backend.model.common.ApiResponse;
import hcmute.lp.backend.model.dto.supplier.SupplierDto;
import hcmute.lp.backend.model.dto.supplier.SupplierRequest;
import hcmute.lp.backend.service.SupplierService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/suppliers")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Supplier API")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    // GET PAGINATED SUPPLIERS
    @GetMapping("/pagination")
    public ResponseEntity<ApiResponse<Page<SupplierDto>>> getSuppliersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "companyName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(required = false) String keyword) {
        Page<SupplierDto> supplierPage = supplierService.getSuppliersPaginated(
                page, size, sortBy, sortDirection, keyword);
        return ResponseEntity.ok(ApiResponse.success("Suppliers retrieved successfully", supplierPage));
    }

    // GET ALL SUPPLIERS
    @GetMapping
    public ResponseEntity<ApiResponse<List<SupplierDto>>> getAllSuppliers() {
        List<SupplierDto> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(ApiResponse.success("Suppliers retrieved successfully", suppliers));
    }

    // GET SUPPLIER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SupplierDto>> getSupplierById(@PathVariable int id) {
        SupplierDto supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier found", supplier));
    }

    // CREATE SUPPLIER
    @PostMapping
    public ResponseEntity<ApiResponse<SupplierDto>> createSupplier(@Valid @RequestBody SupplierRequest supplierRequest) {
        SupplierDto createdSupplier = supplierService.createSupplier(supplierRequest);
        return ResponseEntity.status(201).body(ApiResponse.success("Supplier created", createdSupplier));
    }

    // UPDATE SUPPLIER
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SupplierDto>> updateSupplier(
            @PathVariable int id,
            @Valid @RequestBody SupplierRequest supplierRequest) {
        SupplierDto updatedSupplier = supplierService.updateSupplier(id, supplierRequest);
        return ResponseEntity.ok(ApiResponse.success("Supplier updated successfully", updatedSupplier));
    }

    // DELETE SUPPLIER
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSupplier(@PathVariable int id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier deleted successfully", null));
    }

    // UPDATE SUPPLIER STATUS
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<SupplierDto>> updateSupplierStatus(
            @PathVariable int id,
            @RequestBody Map<String, String> statusRequest) {
        String status = statusRequest.get("status");
        SupplierDto updatedSupplier = supplierService.updateSupplierStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Supplier status updated successfully", updatedSupplier));
    }

    // UPDATE SUPPLIER CATEGORIES
    @PatchMapping("/{id}/categories")
    public ResponseEntity<ApiResponse<SupplierDto>> updateSupplierCategories(
            @PathVariable int id,
            @RequestBody Set<Integer> categoryIds) {
        SupplierDto updatedSupplier = supplierService.updateSupplierCategories(id, categoryIds);
        return ResponseEntity.ok(ApiResponse.success("Supplier categories updated successfully", updatedSupplier));
    }
}