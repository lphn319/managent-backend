package hcmute.lp.backend.service;

import hcmute.lp.backend.model.dto.role.RoleDto;
import hcmute.lp.backend.model.dto.role.RoleRequest;

import java.util.List;

public interface RoleService {
    List<RoleDto> getAllRoles();
    RoleDto getRoleById(Long id);
    RoleDto createRole(RoleRequest roleRequest);
    RoleDto updateRole(Long id, RoleRequest roleRequest);
    void deleteRole(Long id);
    boolean existsById(Long id);
    boolean existsByName(String name);
    RoleDto getRoleByName(String name);
}