package hcmute.lp.backend.model.mapper;

import hcmute.lp.backend.model.dto.department.DepartmentDto;
import hcmute.lp.backend.model.dto.department.DepartmentRequest;
import hcmute.lp.backend.model.entity.Department;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepartmentMapper {

    DepartmentMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(DepartmentMapper.class);

    @Mapping(target = "employees", ignore = true)
    Department toEntity(DepartmentRequest departmentRequest);

    DepartmentDto toDto(Department department);

    List<DepartmentDto> toDtoList(List<Department> departments);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employees", ignore = true)
    void updateEntityFromRequest(DepartmentRequest departmentRequest, @MappingTarget Department department);
}