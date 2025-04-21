package hcmute.lp.backend.controller;

import hcmute.lp.backend.model.common.ApiResponse;
import hcmute.lp.backend.model.dto.user.UserDto;
import hcmute.lp.backend.model.dto.user.UserRequest;
import hcmute.lp.backend.security.annotation.HasAnyRole;
import hcmute.lp.backend.service.EmployeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/employees")
@CrossOrigin(origins = "http://localhost:4200")
@HasAnyRole({"ADMIN", "MANAGER"})
@Tag(name = "Employee API")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    @HasAnyRole({"ADMIN", "MANAGER"})
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllEmployees() {
        List<UserDto> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách nhân viên thành công", employees));
    }

    @GetMapping("/{id}")
    @HasAnyRole({"ADMIN", "MANAGER"})
    public ResponseEntity<ApiResponse<UserDto>> getEmployeeById(@PathVariable Long id) {
        UserDto employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin nhân viên thành công", employee));
    }

    @PostMapping
    @HasAnyRole({"ADMIN"})
    public ResponseEntity<ApiResponse<UserDto>> createEmployee(@Valid @RequestBody UserRequest employeeRequest) {
        UserDto createdEmployee = employeeService.createEmployee(employeeRequest);
        return new ResponseEntity<>(ApiResponse.success("Tạo nhân viên thành công", createdEmployee), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @HasAnyRole({"ADMIN", "MANAGER"})
    public ResponseEntity<ApiResponse<UserDto>> updateEmployee(@PathVariable Long id, @Valid @RequestBody UserRequest employeeRequest) {
        UserDto updatedEmployee = employeeService.updateEmployee(id, employeeRequest);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thông tin nhân viên thành công", updatedEmployee));
    }

    @DeleteMapping("/{id}")
    @HasAnyRole({"ADMIN"})
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa nhân viên thành công", null));
    }

    @PatchMapping("/{id}/status")
    @HasAnyRole({"ADMIN"})
    public ResponseEntity<ApiResponse<Void>> changeEmployeeStatus(@PathVariable Long id, @RequestParam boolean isActive) {
        employeeService.changeEmployeeStatus(id, isActive);
        String message = isActive ? "Kích hoạt tài khoản nhân viên thành công" : "Vô hiệu hóa tài khoản nhân viên thành công";
        return ResponseEntity.ok(ApiResponse.success(message, null));
    }

    @GetMapping("/statistics")
    @HasAnyRole({"ADMIN", "MANAGER"})
    public ResponseEntity<ApiResponse<Map<String, Object>>> getEmployeeStatistics() {
        Map<String, Object> statistics = employeeService.getEmployeeStatistics();
        return ResponseEntity.ok(ApiResponse.success("Lấy thống kê nhân viên thành công", statistics));
    }

    @GetMapping("/department/{departmentId}")
    @HasAnyRole({"ADMIN", "MANAGER"})
    public ResponseEntity<ApiResponse<List<UserDto>>> getEmployeesByDepartment(@PathVariable Integer departmentId) {
        List<UserDto> employees = employeeService.getEmployeesByDepartment(departmentId);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách nhân viên theo phòng ban thành công", employees));
    }

    @GetMapping("/status")
    @HasAnyRole({"ADMIN", "MANAGER"})
    public ResponseEntity<ApiResponse<List<UserDto>>> getEmployeesByStatus(@RequestParam boolean isActive) {
        List<UserDto> employees = employeeService.getEmployeesByStatus(isActive);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách nhân viên theo trạng thái thành công", employees));
    }
}