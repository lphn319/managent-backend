package hcmute.lp.backend.service;

import hcmute.lp.backend.model.dto.user.UserDto;
import hcmute.lp.backend.model.dto.user.UserRequest;

import java.util.List;
import java.util.Map;

public interface EmployeeService {
    List<UserDto> getAllEmployees();
    UserDto getEmployeeById(Long id);
    UserDto createEmployee(UserRequest employeeRequest);
    UserDto updateEmployee(Long id, UserRequest employeeRequest);
    void deleteEmployee(Long id);
    void changeEmployeeStatus(Long id, boolean isActive);

    // Các phương thức bổ sung
    Map<String, Object> getEmployeeStatistics();
    List<UserDto> getEmployeesByDepartment(Integer departmentId);
    List<UserDto> getEmployeesByStatus(boolean isActive);
}
