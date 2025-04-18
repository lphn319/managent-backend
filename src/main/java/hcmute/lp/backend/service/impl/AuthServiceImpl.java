package hcmute.lp.backend.service.impl;

import hcmute.lp.backend.exception.UnauthorizedException;
import hcmute.lp.backend.model.dto.auth.AuthResponse;
import hcmute.lp.backend.model.dto.auth.LoginRequest;
import hcmute.lp.backend.model.dto.auth.RegisterRequest;
import hcmute.lp.backend.model.entity.Role;
import hcmute.lp.backend.model.entity.User;
import hcmute.lp.backend.model.mapper.UserMapper;
import hcmute.lp.backend.repository.RoleRepository;
import hcmute.lp.backend.repository.UserRepository;
import hcmute.lp.backend.security.CustomUserDetails;
import hcmute.lp.backend.security.JwtService;
import hcmute.lp.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        try {
            // Xác thực thông tin đăng nhập
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // Lấy thông tin người dùng sau khi xác thực thành công
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();

            // Kiểm tra trạng thái hoạt động của tài khoản
            if (!user.isActive()) {
                throw new UnauthorizedException("Tài khoản đã bị vô hiệu hóa");
            }

            // Kiểm tra cờ rememberMe và sử dụng giá trị mặc định false nếu null
            boolean rememberMe = loginRequest.getRememberMe() != null && loginRequest.getRememberMe();

            // Tạo token JWT với thời hạn dựa trên rememberMe
            String accessToken = jwtService.generateToken(userDetails, rememberMe);
            String refreshToken = jwtService.generateRefreshToken(userDetails, rememberMe);

            // Ghi log sự kiện đăng nhập thành công
            log.info("Đăng nhập thành công: {} với cờ rememberMe={}", loginRequest.getEmail(), rememberMe);

            // Tạo đối tượng phản hồi
            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .user(userMapper.toDto(user))
                    .build();

        } catch (BadCredentialsException e) {
            log.error("Đăng nhập thất bại với email: {}", loginRequest.getEmail());
            throw new UnauthorizedException("Email hoặc mật khẩu không chính xác");
        }
    }

    @Override
    public AuthResponse register(RegisterRequest registerRequest) throws BadRequestException {
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Email đã được sử dụng");
        }

        // Kiểm tra số điện thoại có hợp lệ không (nếu được cung cấp)
        if (registerRequest.getPhoneNumber() != null && !registerRequest.getPhoneNumber().isEmpty()) {
            if (userRepository.existsByPhoneNumber(registerRequest.getPhoneNumber())) {
                throw new BadRequestException("Số điện thoại đã được sử dụng");
            }
        }

        // Lấy vai trò CUSTOMER
        Role customerRole = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Vai trò CUSTOMER không tồn tại"));

        // Tạo đối tượng User từ thông tin đăng ký
        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phoneNumber(registerRequest.getPhoneNumber())
                .active(true)
                .role(customerRole)
                .department(null)
                .createdAt(LocalDateTime.now())
                .build();

        // Lưu người dùng vào cơ sở dữ liệu
        User savedUser = userRepository.save(user);

        // Tạo UserDetails để tạo token
        CustomUserDetails userDetails = new CustomUserDetails(savedUser);

        // Tạo token JWT (không sử dụng rememberMe cho đăng ký)
        String accessToken = jwtService.generateToken(userDetails, false);
        String refreshToken = jwtService.generateRefreshToken(userDetails, false);

        // Ghi log sự kiện đăng ký thành công
        log.info("Đăng ký thành công cho email: {}", registerRequest.getEmail());

        // Tạo đối tượng phản hồi
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userMapper.toDto(savedUser))
                .build();
    }
}