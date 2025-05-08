package hcmute.lp.backend.model.mapper;

import hcmute.lp.backend.model.dto.employee.EmployeeDto;
import hcmute.lp.backend.model.dto.employee.EmployeeRequest;
import hcmute.lp.backend.model.entity.Department;
import hcmute.lp.backend.model.entity.Employee;
import hcmute.lp.backend.model.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {DepartmentMapper.class})
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(EmployeeMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "department", source = "department")
    Employee toEntity(EmployeeRequest employeeRequest, User user, Department department);

    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "status", source = "user.status")
    EmployeeDto toDto(Employee employee);

    List<EmployeeDto> toDtoList(List<Employee> employees);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "department", ignore = true)
    void updateEntityFromRequest(EmployeeRequest employeeRequest, @MappingTarget Employee employee);
}