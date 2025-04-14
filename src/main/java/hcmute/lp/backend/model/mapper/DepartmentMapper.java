package hcmute.lp.backend.model.mapper;

import hcmute.lp.backend.model.dto.department.DepartmentDto;
import hcmute.lp.backend.model.dto.department.DepartmentRequest;
import hcmute.lp.backend.model.entity.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {
    public DepartmentDto toDto(Department department) {
        return DepartmentDto.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .build();
    }

    public Department toEntity(DepartmentRequest departmentRequest) {
        Department department = new Department();
        department.setName(departmentRequest.getName());
        department.setDescription(departmentRequest.getDescription());
        return department;
    }

    public void updateEntityFromRequest(Department department, DepartmentRequest departmentRequest) {
        if (departmentRequest.getName() != null) {
            department.setName(departmentRequest.getName());
        }
        if (departmentRequest.getDescription() != null) {
            department.setDescription(departmentRequest.getDescription());
        }
    }
}
