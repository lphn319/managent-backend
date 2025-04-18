package hcmute.lp.backend.controller;

import hcmute.lp.backend.model.common.ApiResponse;
import hcmute.lp.backend.model.dto.department.DepartmentDto;
import hcmute.lp.backend.model.dto.department.DepartmentRequest;
import hcmute.lp.backend.service.DepartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@Tag(name = "Department API")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    // GET ALL DEPARTMENTS
    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentDto>>> getAllDepartments() {
        List<DepartmentDto> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success("Departments retrieved successfully", departments));
    }

    // GET DEPARTMENT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentDto>> getDepartmentById(@PathVariable int id) {
        DepartmentDto department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(ApiResponse.success("Department found", department));
    }

    // CREATE DEPARTMENT
    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentDto>> createDepartment(@RequestBody DepartmentRequest departmentRequest) {
        DepartmentDto createdDepartment = departmentService.createDepartment(departmentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Department created", createdDepartment));
    }

    // UPDATE DEPARTMENT
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentDto>> updateDepartment(@PathVariable int id, @RequestBody DepartmentRequest departmentRequest) {
        DepartmentDto updatedDepartment = departmentService.updateDepartment(id, departmentRequest);
        return ResponseEntity.ok(ApiResponse.success("Department updated successfully", updatedDepartment));
    }

    // DELETE DEPARTMENT
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable int id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.success("Department deleted successfully", null));
    }
}
