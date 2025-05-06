package hcmute.lp.backend.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hcmute.lp.backend.model.dto.brand.BrandRequest;
import hcmute.lp.backend.model.dto.department.DepartmentRequest;
import hcmute.lp.backend.model.dto.role.RoleRequest;
import hcmute.lp.backend.model.dto.user.UserRequest;
import hcmute.lp.backend.model.entity.*;
import hcmute.lp.backend.model.mapper.RoleMapper;
import hcmute.lp.backend.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Khởi tạo theo thứ tự phụ thuộc
        initializeBrands();
        initializeDepartments();
        initializeRoles();
        initializeUsers();
    }

    private void initializeBrands() {
        if (brandRepository.count() == 0) {
            log.info("Initializing brand data from JSON file...");
            try {
                Resource resource = new ClassPathResource("initial-brands.json");
                List<BrandRequest> brandRequests = loadFromJson(resource, new TypeReference<List<BrandRequest>>() {});

                List<Brand> brands = brandRequests.stream()
                        .map(this::convertToBrand)
                        .toList();

                brandRepository.saveAll(brands);
                log.info("Successfully initialized {} brands", brands.size());
            } catch (IOException e) {
                log.error("Failed to initialize brands from JSON file", e);
            }
        }
    }

    private void initializeDepartments() {
        if (departmentRepository.count() == 0) {
            log.info("Initializing department data from JSON file...");
            try {
                Resource resource = new ClassPathResource("initial-departments.json");
                List<DepartmentRequest> departmentRequests = loadFromJson(resource, new TypeReference<List<DepartmentRequest>>() {});

                List<Department> departments = departmentRequests.stream()
                        .map(this::convertToDepartment)
                        .toList();

                departmentRepository.saveAll(departments);
                log.info("Successfully initialized {} departments", departments.size());
            } catch (IOException e) {
                log.error("Failed to initialize departments from JSON file", e);
            }
        }
    }

    private void initializeRoles() {
        if (roleRepository.count() == 0) {
            log.info("Initializing role data from JSON file...");
            try {
                Resource resource = new ClassPathResource("initial-roles.json");
                List<RoleRequest> roleRequests = loadFromJson(resource, new TypeReference<List<RoleRequest>>() {});

                // Sử dụng RoleMapper để chuyển đổi
                List<Role> roles = roleRequests.stream()
                        .map(roleMapper::toEntity)  // Sử dụng mapper
                        .toList();

                // Lưu roles
                List<Role> savedRoles = roleRepository.saveAll(roles);

                // Log để debug
                log.info("Saved {} roles", savedRoles.size());
                savedRoles.forEach(role ->
                        log.debug("Saved role: id={}, name={}", role.getId(), role.getName()));

                // Verify count
                long count = roleRepository.count();
                log.info("Role count after save: {}", count);

            } catch (IOException e) {
                log.error("Failed to initialize roles from JSON file", e);
            }
        }
    }

    private void initializeUsers() {
        if (userRepository.count() == 0) {
            log.info("Initializing user data from JSON file...");
            try {
                Resource resource = new ClassPathResource("initial-users.json");

                String jsonContent = new String(resource.getInputStream().readAllBytes());
                log.debug("JSON content: {}", jsonContent);

                List<UserRequest> userRequests = loadFromJson(resource, new TypeReference<List<UserRequest>>() {});

                List<User> users = userRequests.stream()
                        .map(this::convertToUser)
                        .toList();

                userRepository.saveAll(users);
                log.info("Successfully initialized {} users", users.size());
            } catch (IOException e) {
                log.error("Failed to initialize users from JSON file", e);
            }
        }
    }

    private <T> T loadFromJson(Resource resource, TypeReference<T> typeReference) throws IOException {
        InputStream inputStream = resource.getInputStream();
        return objectMapper.readValue(inputStream, typeReference);
    }

    private Brand convertToBrand(BrandRequest request) {
        Brand brand = new Brand();
        brand.setName(request.getName());
        brand.setDescription(request.getDescription());
        brand.setLogoUrl(request.getLogoUrl());
        brand.setOrigin(request.getOrigin());
        brand.setWebsite(request.getWebsite());

        try {
            brand.setStatus(Brand.BrandStatus.valueOf(request.getStatus()));
        } catch (IllegalArgumentException e) {
            brand.setStatus(Brand.BrandStatus.ACTIVE);
        }

        return brand;
    }

    private Department convertToDepartment(DepartmentRequest request) {
        Department department = new Department();
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        return department;
    }

    private User convertToUser(UserRequest request) {
        // Phần xử lý vai trò giữ nguyên
        String roleName;
        switch (request.getRoleId().intValue()) {
            case 1:
                roleName = "ADMIN";
                break;
            case 2:
                roleName = "MANAGER";
                break;
            case 3:
                roleName = "EMPLOYEE";
                break;
            case 4:
                roleName = "CUSTOMER";
                break;
            default:
                throw new RuntimeException("Unknown role ID: " + request.getRoleId());
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found with name: " + roleName));

        // Phần xử lý phòng ban - tìm theo tên thay vì ID
        Department department = null;
        if (request.getDepartmentId() != null) {
            // Xác định tên phòng ban dựa vào ID trong request
            String departmentName;
            switch (request.getDepartmentId()) {
                case 1:
                    departmentName = "IT Department";
                    break;
                case 2:
                    departmentName = "HR Department";
                    break;
                case 3:
                    departmentName = "Sales Department";
                    break;
                case 4:
                    departmentName = "Finance Department";
                    break;
                case 5:
                    departmentName = "Operations Department";
                    break;
                default:
                    throw new RuntimeException("Unknown department ID: " + request.getDepartmentId());
            }

            // Tìm phòng ban theo tên
            department = departmentRepository.findByName(departmentName)
                    .orElseThrow(() -> new RuntimeException("Department not found with name: " + departmentName));
        }

        // Phần còn lại giữ nguyên
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setDepartment(department);
        user.setDateOfBirth(request.getDateOfBirth());

        // Xử lý trường gender nếu có
        try {
            if (request.getGender() != null) {
                user.setGender(User.GenderType.valueOf(request.getGender()));
            }
        } catch (IllegalArgumentException e) {
            log.warn("Invalid gender: {}. Setting to null", request.getGender());
        }

        try {
            if (request.getStatus() != null) {
                user.setStatus(User.UserStatus.valueOf(String.valueOf(request.getStatus())));
            } else {
                user.setStatus(User.UserStatus.ACTIVE);
            }
        } catch (IllegalArgumentException e) {
            user.setStatus(User.UserStatus.ACTIVE);
        }

        return user;
    }
}