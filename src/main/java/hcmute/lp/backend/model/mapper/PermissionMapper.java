package hcmute.lp.backend.model.mapper;

import hcmute.lp.backend.model.dto.permission.PermissionDto;
import hcmute.lp.backend.model.dto.permission.PermissionRequest;
import hcmute.lp.backend.model.entity.Permission;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper {

    PermissionMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(PermissionMapper.class);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "users", ignore = true)
    Permission toEntity(PermissionRequest permissionRequest);

    PermissionDto toDto(Permission permission);

    List<PermissionDto> toDtoList(List<Permission> permissions);
    Set<PermissionDto> toDtoSet(Set<Permission> permissions);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "users", ignore = true)
    void updateEntityFromRequest(PermissionRequest permissionRequest, @MappingTarget Permission permission);
}