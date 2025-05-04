package hcmute.lp.backend.controller;

import hcmute.lp.backend.model.common.ApiResponse;
import hcmute.lp.backend.model.dto.import_.ImportDto;
import hcmute.lp.backend.model.dto.import_.ImportRequest;
import hcmute.lp.backend.service.ImportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/imports")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Import API")
public class ImportController {
    @Autowired
    private ImportService importService;

    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<ImportDto>>> getImportsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) Long supplierId, // Thay đổi từ Integer sang Long
            @RequestParam(required = false) Long employeeId, // Thay đổi từ Integer sang Long
            @RequestParam(required = false) String status) {

        Page<ImportDto> importsPage = importService.getImportsPaginated(
                page, size, sortBy, sortDirection, supplierId, employeeId, status);

        return ResponseEntity.ok(ApiResponse.success("Imports retrieved successfully", importsPage));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ImportDto>>> getAllImports() {
        List<ImportDto> imports = importService.getAllImports();
        return ResponseEntity.ok(ApiResponse.success("Imports retrieved successfully", imports));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ImportDto>> getImportById(@PathVariable Long id) { // Thay đổi từ int sang Long
        ImportDto importDto = importService.getImportById(id);
        return ResponseEntity.ok(ApiResponse.success("Import found", importDto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ImportDto>> createImport(@Valid @RequestBody ImportRequest importRequest) {
        ImportDto createdImport = importService.createImport(importRequest);
        return ResponseEntity.status(201).body(ApiResponse.success("Import created", createdImport));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ImportDto>> updateImport(
            @PathVariable Long id, @Valid @RequestBody ImportRequest importRequest) { // Thay đổi từ int sang Long
        ImportDto updatedImport = importService.updateImport(id, importRequest);
        return ResponseEntity.ok(ApiResponse.success("Import updated successfully", updatedImport));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ImportDto>> updateStatus(
            @PathVariable Long id, @RequestParam String status) { // Thay đổi từ int sang Long
        ImportDto updatedImport = importService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Import status updated successfully", updatedImport));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteImport(@PathVariable Long id) { // Thay đổi từ int sang Long
        importService.deleteImport(id);
        return ResponseEntity.ok(ApiResponse.success("Import deleted successfully", null));
    }
}