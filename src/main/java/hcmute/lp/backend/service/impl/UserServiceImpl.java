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

import hcmute.lp.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;

    @Override
    @Transactional
    public UserDto createUser(UserRequest userRequest) {
        // Kiểm tra xem email đã tồn tại chưa
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        // Kiểm tra xem số điện thoại đã tồn tại chưa
        if (userRepository.existsByPhoneNumber(userRequest.getPhoneNumber())) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }

        // Lấy role và department từ request
        Role role = roleRepository.findById(userRequest.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vai trò"));

        Department department = departmentRepository.findById(userRequest.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng ban"));

        // Mã hóa mật khẩu
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        // Chuyển đổi request thành entity
        User user = userMapper.toEntity(userRequest, role, department);

        // Lưu vào database
        user = userRepository.save(user);

        // Chuyển đổi entity thành dto để trả về
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UserRequest userRequest) {
        // Lấy thông tin người dùng
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        // Kiểm tra quyền
        if (!securityUtils.canModifyResource(user.getId()) && !securityUtils.hasRole("ADMIN")) {
            throw new UnauthorizedException("Bạn không có quyền cập nhật thông tin người dùng này");
        }

        // Kiểm tra email đã tồn tại chưa (nếu đổi email)
        if (!user.getEmail().equals(userRequest.getEmail()) &&
                userRepository.existsByEmail(userRequest.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        // Kiểm tra số điện thoại đã tồn tại chưa (nếu đổi số điện thoại)
        if (!user.getPhoneNumber().equals(userRequest.getPhoneNumber()) &&
                userRepository.existsByPhoneNumber(userRequest.getPhoneNumber())) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }

        // Lấy role và department từ request
        Role role = roleRepository.findById(userRequest.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vai trò"));

        Department department = departmentRepository.findById(userRequest.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng ban"));

        // Mã hóa mật khẩu nếu có thay đổi
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        } else {
            userRequest.setPassword(user.getPassword());
        }

        // Cập nhật thông tin người dùng
        userMapper.updateUserFromRequest(userRequest, user, role, department);

        // Lưu vào database
        user = userRepository.save(user);

        // Chuyển đổi entity thành dto để trả về
        return userMapper.toDto(user);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        // Kiểm tra quyền
        if (!securityUtils.hasRole("ADMIN")) {
            throw new UnauthorizedException("Bạn không có quyền xóa người dùng");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        // Xóa người dùng
        userRepository.delete(user);
    }

    @Override
    public UserDto getCurrentUser() {
        User user = securityUtils.getCurrentUser();
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void changeUserStatus(Long id, boolean isActive) {
        // Kiểm tra quyền
        if (!securityUtils.hasRole("ADMIN")) {
            throw new UnauthorizedException("Bạn không có quyền thay đổi trạng thái người dùng");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        user.setActive(isActive);
        userRepository.save(user);
    }
}