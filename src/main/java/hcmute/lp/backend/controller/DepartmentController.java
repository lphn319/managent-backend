package hcmute.lp.backend.controller;

import hcmute.lp.backend.model.common.ApiResponse;
import hcmute.lp.backend.model.dto.department.DepartmentDto;
import hcmute.lp.backend.model.dto.department.DepartmentRequest;
import hcmute.lp.backend.security.annotation.HasAnyRole;
import hcmute.lp.backend.service.DepartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@CrossOrigin(origins = "http://localhost:4200")
@HasAnyRole({"ADMIN", "MANAGER"})
@Tag(name = "Department API")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentDto>>> getAllDepartments() {
        List<DepartmentDto> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success("Departments retrieved successfully", departments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentDto>> getDepartmentById(@PathVariable int id) {
        DepartmentDto department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(ApiResponse.success("Department found", department));
    }

    @PostMapping
//    @HasAnyRole({"ADMIN"})
    public ResponseEntity<ApiResponse<DepartmentDto>> createDepartment(@Valid @RequestBody DepartmentRequest departmentRequest) {
        DepartmentDto createdDepartment = departmentService.createDepartment(departmentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Department created", createdDepartment));
    }

    @PutMapping("/{id}")
    @HasAnyRole({"ADMIN"})
    public ResponseEntity<ApiResponse<DepartmentDto>> updateDepartment(@PathVariable int id, @Valid @RequestBody DepartmentRequest departmentRequest) {
        DepartmentDto updatedDepartment = departmentService.updateDepartment(id, departmentRequest);
        return ResponseEntity.ok(ApiResponse.success("Department updated successfully", updatedDepartment));
    }

    @DeleteMapping("/{id}")
    @HasAnyRole({"ADMIN"})
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable int id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.success("Department deleted successfully", null));
    }
}