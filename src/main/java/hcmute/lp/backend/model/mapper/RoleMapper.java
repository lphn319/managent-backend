package hcmute.lp.backend.model.mapper;

import hcmute.lp.backend.model.dto.role.RoleDto;
import hcmute.lp.backend.model.dto.role.RoleRequest;
import hcmute.lp.backend.model.entity.Role;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    RoleMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(RoleMapper.class);

    @Mapping(target = "user", ignore = true)
    Role toEntity(RoleRequest roleRequest);

    RoleDto toDto(Role role);

    List<RoleDto> toDtoList(List<Role> roles);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromRequest(RoleRequest roleRequest, @MappingTarget Role role);
}