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
import org.springframework.data.domain.Pageable;
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
    public Page<UserDto> getEmployeesPaginated(
            int page,
            int size,
            String sortBy,
            String sortDirection,
            String keyword,
            Integer departmentId,
            Boolean isActive) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        // Lấy các vai trò nhân viên (EMPLOYEE, MANAGER)
        List<Role> employeeRoles = roleRepository.findByNameIn(Arrays.asList("EMPLOYEE", "MANAGER"));

        Page<User> employeePage;

        // Chuyển đổi Boolean isActive thành User.UserStatus
        User.UserStatus status = null;
        if (isActive != null) {
            status = isActive ? User.UserStatus.ACTIVE : User.UserStatus.INACTIVE;
        }

        // Xử lý các trường hợp lọc khác nhau
        if (keyword != null && !keyword.isEmpty() && departmentId != null && status != null) {
            // Lọc theo từ khóa, phòng ban và trạng thái
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng ban"));
            employeePage = userRepository.findByKeywordAndRoleInAndDepartmentAndStatus(
                    keyword, employeeRoles, department, status, pageable);
        } else if (keyword != null && !keyword.isEmpty() && status != null) {
            // Lọc theo từ khóa và trạng thái
            employeePage = userRepository.findByKeywordAndRoleInAndStatus(
                    keyword, employeeRoles, status, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            // Lọc chỉ theo từ khóa
            employeePage = userRepository.findByKeywordAndRoleIn(keyword, employeeRoles, pageable);
        } else if (departmentId != null) {
            // Lọc chỉ theo phòng ban
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng ban"));
            employeePage = userRepository.findByDepartment(department, pageable);
        } else if (status != null) {
            // Lọc chỉ theo trạng thái
            employeePage = userRepository.findByStatus(status, pageable);
        } else {
            // Không có điều kiện lọc, trả về tất cả nhân viên
            employeePage = userRepository.findByRoleIn(employeeRoles, pageable);
        }

        // Chuyển đổi Page<User> thành Page<UserDto>
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
        String encodedPassword = passwordEncoder.encode(employeeRequest.getPassword());
        employeeRequest.setPassword(encodedPassword);

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

        // THÊM: Cập nhật status nếu có trong request
        if (employeeRequest.getStatus() != null) {
            employee.setStatus(employeeRequest.getStatus());
        }

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
    public void changeEmployeeStatus(Long id, User.UserStatus status) {
        // Kiểm tra quyền
        if (!securityUtils.hasRole("ADMIN")) {
            throw new UnauthorizedException("Bạn không có quyền thay đổi trạng thái nhân viên");
        }

        User employee = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên"));

        employee.setStatus(status);
        userRepository.save(employee);
    }

    @Override
    public Map<String, Object> getEmployeeStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // Lấy các vai trò nhân viên (EMPLOYEE, MANAGER)
        List<Role> employeeRoles = roleRepository.findByNameIn(Arrays.asList("EMPLOYEE", "MANAGER"));
        List<User> employees = userRepository.findByRoleIn(employeeRoles);

        // Tổng số nhân viên
        int totalEmployees = employees.size();
        statistics.put("total", totalEmployees);

        // Số nhân viên đang hoạt động
        int activeEmployees = (int) employees.stream()
                .filter(e -> e.getStatus() == User.UserStatus.ACTIVE)
                .count();
        statistics.put("active", activeEmployees);

        // Số nhân viên không hoạt động
        int inactiveEmployees = totalEmployees - activeEmployees;
        statistics.put("inactive", inactiveEmployees);

        // Thống kê theo phòng ban
        List<Map<String, Object>> departmentCountsList = userRepository.getDepartmentCounts();
        Map<String, Integer> departmentStats = new HashMap<>();

        for (Map<String, Object> item : departmentCountsList) {
            String deptName = (String) item.get("departmentName");
            Integer count = ((Number) item.get("count")).intValue();
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
        User.UserStatus status = isActive ? User.UserStatus.ACTIVE : User.UserStatus.INACTIVE;
        return userRepository.findByStatus(status).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}