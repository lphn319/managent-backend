package hcmute.lp.backend.service.impl;

import hcmute.lp.backend.exception.ResourceNotFoundException;
import hcmute.lp.backend.model.dto.department.DepartmentDto;
import hcmute.lp.backend.model.dto.department.DepartmentRequest;
import hcmute.lp.backend.model.entity.Department;
import hcmute.lp.backend.model.mapper.DepartmentMapper;
import hcmute.lp.backend.repository.DepartmentRepository;
import hcmute.lp.backend.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<DepartmentDto> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                .map(departmentMapper::toDto)
                .toList();
    }

    @Override
    public DepartmentDto getDepartmentById(int id) {
        Department department = departmentRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Department not found with id: " + id));
        return departmentMapper.toDto(department);
    }

    @Override
    public DepartmentDto createDepartment(DepartmentRequest departmentRequest) {
        if (departmentRepository.existsByName(departmentRequest.getName())) {
            throw new IllegalArgumentException("Department already exists with name: " + departmentRequest.getName());
        }
        Department department = departmentMapper.toEntity(departmentRequest);
        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toDto(savedDepartment);
    }

    @Override
    public DepartmentDto updateDepartment(int id, DepartmentRequest departmentRequest) {
        Department department = departmentRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Department not found with id: " + id));

        if (!department.getName().equals(departmentRequest.getName()) && departmentRepository.existsByName(departmentRequest.getName())) {
            throw new IllegalArgumentException("Department with this name already exists: " + departmentRequest.getName());
        }
        departmentMapper.updateEntityFromRequest(departmentRequest, department);
        Department updatedDepartment = departmentRepository.save(department);
        return departmentMapper.toDto(updatedDepartment);
    }

    @Override
    public void deleteDepartment(int id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }

    @Override
    public boolean existsById(int id) {
        return departmentRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return departmentRepository.existsByName(name);
    }

}
