// UserMapper.java
package hcmute.lp.backend.model.mapper;

import hcmute.lp.backend.model.dto.user.UserDto;
import hcmute.lp.backend.model.dto.user.UserRequest;
import hcmute.lp.backend.model.entity.Department;
import hcmute.lp.backend.model.entity.Role;
import hcmute.lp.backend.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", source = "role")
    @Mapping(target = "name", source = "userRequest.name")
    @Mapping(target = "status", constant = "ACTIVE")
    User toEntity(UserRequest userRequest, Role role, Department department);

    @Mapping(target = "roleName", source = "role.name")
    @Mapping(target = "status", source = "status")
    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "userRequest.name", nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "role", source = "role")
    void updateUserFromRequest(UserRequest userRequest, @MappingTarget User user, Role role, Department department);
}