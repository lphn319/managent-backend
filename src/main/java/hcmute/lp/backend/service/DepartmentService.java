package hcmute.lp.backend.service;

import hcmute.lp.backend.model.dto.department.DepartmentDto;
import hcmute.lp.backend.model.dto.department.DepartmentRequest;
import hcmute.lp.backend.model.entity.Department;

import java.util.List;

public interface DepartmentService {
    List<DepartmentDto> getAllDepartments();
    DepartmentDto getDepartmentById(int id);
    DepartmentDto createDepartment(DepartmentRequest departmentRequest);
    DepartmentDto updateDepartment(int id, DepartmentRequest departmentRequest);
    void deleteDepartment(int id);
    boolean existsById(int id);
    boolean existsByName(String name);
}
