package hcmute.lp.backend.service.impl;

import hcmute.lp.backend.exception.ResourceNotFoundException;
import hcmute.lp.backend.exception.UnauthorizedException;
import hcmute.lp.backend.model.dto.user.UserDto;
import hcmute.lp.backend.model.dto.user.UserRequest;
import hcmute.lp.backend.model.entity.Department;
import hcmute.lp.backend.model.entity.Role;
import hcmute.lp.backend.model.entity.User;
import hcmute.lp.backend.model.mapper.UserMapper;
import hcmute.lp.backend.repository.DepartmentRepository;
import hcmute.lp.backend.repository.RoleRepository;
import hcmute.lp.backend.repository.UserRepository;
import hcmute.lp.backend.security.SecurityUtils;
import hcmute.lp.backend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityUtils securityUtils;

    @Override
    public Page<UserDto> getEmployeePaginated(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Page<User> employeePage = userRepository.findByNameIn(Arrays.asList("EMPLOYEE", "MANAGER"),
                PageRequest.of(page, size, Sort.by(direction, sortBy)));
        return employeePage.map(userMapper::toDto);
    }

    @Override
    public List<UserDto> getAllEmployees() {
        // Lấy role "EMPLOYEE" và "MANAGER"
        List<Role> employeeRoles = roleRepository.findByNameIn(Arrays.asList("EMPLOYEE", "MANAGER"));

        return userRepository.findByRoleIn(employeeRoles).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getEmployeeById(Long id) {
        User employee = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên"));
        return userMapper.toDto(employee);
    }

    @Override
    @Transactional
    public UserDto createEmployee(UserRequest employeeRequest) {
        // Kiểm tra email và số điện thoại
        if (userRepository.existsByEmail(employeeRequest.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        if (userRepository.existsByPhoneNumber(employeeRequest.getPhoneNumber())) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }

        // Lấy role và department
        Role role = roleRepository.findById(employeeRequest.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vai trò"));

        Department department = departmentRepository.findById(employeeRequest.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng ban"));

        // Mã hóa mật khẩu
        employeeRequest.setPassword(passwordEncoder.encode(employeeRequest.getPassword()));

        // Chuyển thành entity và lưu
        User employee = userMapper.toEntity(employeeRequest, role, department);
        employee = userRepository.save(employee);

        return userMapper.toDto(employee);
    }

    @Override
    @Transactional
    public UserDto updateEmployee(Long id, UserRequest employeeRequest) {
        // Kiểm tra sự tồn tại của nhân viên
        User employee = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên"));

        // Kiểm tra quyền
        if (!securityUtils.canModifyResource(employee.getId()) && !securityUtils.hasRole("ADMIN")) {
            throw new UnauthorizedException("Bạn không có quyền cập nhật thông tin nhân viên này");
        }

        // Kiểm tra email và số điện thoại nếu thay đổi
        if (!employee.getEmail().equals(employeeRequest.getEmail()) &&
                userRepository.existsByEmail(employeeRequest.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        if (!employee.getPhoneNumber().equals(employeeRequest.getPhoneNumber()) &&
                userRepository.existsByPhoneNumber(employeeRequest.getPhoneNumber())) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }

        // Lấy role và department mới
        Role role = roleRepository.findById(employeeRequest.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vai trò"));

        Department department = departmentRepository.findById(employeeRequest.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng ban"));

        // Xử lý mật khẩu nếu cần
        if (employeeRequest.getPassword() != null && !employeeRequest.getPassword().isEmpty()) {
            employeeRequest.setPassword(passwordEncoder.encode(employeeRequest.getPassword()));
        } else {
            employeeRequest.setPassword(employee.getPassword());
        }

        // Cập nhật thông tin
        userMapper.updateUserFromRequest(employeeRequest, employee, role, department);
        employee = userRepository.save(employee);

        return userMapper.toDto(employee);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        // Kiểm tra quyền
        if (!securityUtils.hasRole("ADMIN")) {
            throw new UnauthorizedException("Bạn không có quyền xóa nhân viên");
        }

        User employee = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên"));

        // Kiểm tra có phải là admin duy nhất không
        if (employee.getRole().getName().equals("ADMIN")) {
            long adminCount = userRepository.countByRoleName("ADMIN");
            if (adminCount <= 1) {
                throw new IllegalStateException("Không thể xóa admin duy nhất trong hệ thống");
            }
        }

        userRepository.delete(employee);
    }

    @Override
    @Transactional
    public void changeEmployeeStatus(Long id, boolean isActive) {
        // Kiểm tra quyền
        if (!securityUtils.hasRole("ADMIN")) {
            throw new UnauthorizedException("Bạn không có quyền thay đổi trạng thái nhân viên");
        }

        User employee = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên"));

        employee.setActive(isActive);
        userRepository.save(employee);
    }

    @Override
    public Map<String, Object> getEmployeeStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // Tổng số nhân viên
        long totalEmployees = userRepository.count();
        statistics.put("total", totalEmployees);

        // Số nhân viên đang hoạt động
        long activeEmployees = userRepository.countByActiveTrue();
        statistics.put("active", activeEmployees);

        // Số nhân viên không hoạt động
        long inactiveEmployees = userRepository.countByActiveFalse();
        statistics.put("inactive", inactiveEmployees);

        // Thống kê theo phòng ban
        List<Map<String, Object>> departmentCountsList = userRepository.getDepartmentCounts();
        Map<String, Long> departmentStats = new HashMap<>();

        for (Map<String, Object> item : departmentCountsList) {
            String deptName = (String) item.get("departmentName");
            Long count = ((Number) item.get("count")).longValue();
            departmentStats.put(deptName, count);
        }

        statistics.put("departments", departmentStats);

        return statistics;
    }

    @Override
    public List<UserDto> getEmployeesByDepartment(Integer departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng ban"));

        return userRepository.findByDepartment(department).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getEmployeesByStatus(boolean isActive) {
        return userRepository.findByActive(isActive).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}