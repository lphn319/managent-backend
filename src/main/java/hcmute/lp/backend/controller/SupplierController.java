package hcmute.lp.backend.controller;

import hcmute.lp.backend.model.common.ApiResponse;
import hcmute.lp.backend.model.dto.supplier.SupplierDto;
import hcmute.lp.backend.model.dto.supplier.SupplierRequest;
import hcmute.lp.backend.service.SupplierService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Supplier API")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

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
    public ResponseEntity<ApiResponse<SupplierDto>> createSupplier(@RequestBody SupplierRequest supplierRequest) {
        SupplierDto createdSupplier = supplierService.createSupplier(supplierRequest);
        return ResponseEntity.status(201).body(ApiResponse.success("Supplier created", createdSupplier));
    }

    // UPDATE SUPPLIER
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SupplierDto>> updateSupplier(@PathVariable int id, @RequestBody SupplierRequest supplierRequest) {
        SupplierDto updatedSupplier = supplierService.updateSupplier(id, supplierRequest);
        return ResponseEntity.ok(ApiResponse.success("Supplier updated successfully", updatedSupplier));
    }

    // DELETE SUPPLIER
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSupplier(@PathVariable int id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier deleted successfully", null));
    }

}
