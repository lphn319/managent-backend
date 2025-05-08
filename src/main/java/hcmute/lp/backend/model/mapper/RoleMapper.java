package hcmute.lp.backend.model.mapper;

import hcmute.lp.backend.model.dto.permission.PermissionDto;
import hcmute.lp.backend.model.dto.role.RoleDto;
import hcmute.lp.backend.model.dto.role.RoleRequest;
import hcmute.lp.backend.model.entity.Permission;
import hcmute.lp.backend.model.entity.Role;
import hcmute.lp.backend.repository.PermissionRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {PermissionMapper.class})
public abstract class RoleMapper {

    @Autowired
    protected PermissionRepository permissionRepository;

    public abstract RoleDto toDto(Role role);

    public abstract List<RoleDto> toDtoList(List<Role> roles);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "permissions", expression = "java(mapPermissions(roleRequest.getPermissionIds()))")
    public abstract Role toEntity(RoleRequest roleRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "name", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "description", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "permissions", expression = "java(roleRequest.getPermissionIds() != null ? mapPermissions(roleRequest.getPermissionIds()) : role.getPermissions())")
    public abstract void updateEntityFromRequest(@MappingTarget Role role, RoleRequest roleRequest);

    // Helper method để map từ danh sách id của permission sang danh sách entity Permission
    protected Set<Permission> mapPermissions(Set<Integer> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            return new HashSet<>();
        }
        return permissionIds.stream()
                .map(id -> permissionRepository.findById(id).orElse(null))
                .filter(permission -> permission != null)
                .collect(Collectors.toSet());
    }
}