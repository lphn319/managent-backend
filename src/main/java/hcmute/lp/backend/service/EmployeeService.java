package hcmute.lp.backend.service;

import hcmute.lp.backend.model.dto.user.UserDto;
import hcmute.lp.backend.model.dto.user.UserRequest;
import hcmute.lp.backend.model.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

    // Phương thức phân trang và tìm kiếm
    Page<UserDto> getEmployeesPaginated(
            int page,
            int size,
            String sortBy,
            String sortDirection,
            String keyword,
            Integer departmentId,
            Boolean isActive);

    List<UserDto> getAllEmployees();

    UserDto getEmployeeById(Long id);

    UserDto createEmployee(UserRequest employeeRequest);

    UserDto updateEmployee(Long id, UserRequest employeeRequest);

    void deleteEmployee(Long id);

    void changeEmployeeStatus(Long id, User.UserStatus status);

    Map<String, Object> getEmployeeStatistics();

    List<UserDto> getEmployeesByDepartment(Integer departmentId);

    List<UserDto> getEmployeesByStatus(boolean isActive);
}