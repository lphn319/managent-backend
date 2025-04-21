package hcmute.lp.backend.controller;

import hcmute.lp.backend.model.common.ApiResponse;
import hcmute.lp.backend.model.dto.role.RoleDto;
import hcmute.lp.backend.model.dto.role.RoleRequest;
import hcmute.lp.backend.security.annotation.HasAnyRole;
import hcmute.lp.backend.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@CrossOrigin(origins = "http://localhost:4200")
@HasAnyRole({"ADMIN", "MANAGER"})
@Tag(name = "Role API")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    @HasAnyRole({"ADMIN"})
    public ResponseEntity<ApiResponse<List<RoleDto>>> getAllRoles() {
        List<RoleDto> roles = roleService.getAllRoles();
        return ResponseEntity.ok(ApiResponse.success("Roles retrieved successfully", roles));
    }

    @GetMapping("/{id}")
    @HasAnyRole({"ADMIN"})
    public ResponseEntity<ApiResponse<RoleDto>> getRoleById(@PathVariable Long id) {
        RoleDto role = roleService.getRoleById(id);
        return ResponseEntity.ok(ApiResponse.success("Role found", role));
    }

    @PostMapping
    @HasAnyRole({"ADMIN"})
    public ResponseEntity<ApiResponse<RoleDto>> createRole(@Valid @RequestBody RoleRequest roleRequest) {
        RoleDto createdRole = roleService.createRole(roleRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Role created successfully", createdRole));
    }

    @PutMapping("/{id}")
    @HasAnyRole({"ADMIN"})
    public ResponseEntity<ApiResponse<RoleDto>> updateRole(
            @PathVariable Long id, @Valid @RequestBody RoleRequest roleRequest) {
        RoleDto updatedRole = roleService.updateRole(id, roleRequest);
        return ResponseEntity.ok(ApiResponse.success("Role updated successfully", updatedRole));
    }

    @DeleteMapping("/{id}")
    @HasAnyRole({"ADMIN"})
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok(ApiResponse.success("Role deleted successfully", null));
    }

    @GetMapping("/name/{name}")
    @HasAnyRole({"ADMIN"})
    public ResponseEntity<ApiResponse<RoleDto>> getRoleByName(@PathVariable String name) {
        RoleDto role = roleService.getRoleByName(name);
        return ResponseEntity.ok(ApiResponse.success("Role found", role));
    }
}