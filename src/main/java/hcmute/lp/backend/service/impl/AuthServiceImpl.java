package hcmute.lp.backend.service.impl;

import hcmute.lp.backend.exception.UnauthorizedException;
import hcmute.lp.backend.model.dto.auth.AuthResponse;
import hcmute.lp.backend.model.dto.auth.LoginRequest;
import hcmute.lp.backend.model.entity.User;
import hcmute.lp.backend.model.mapper.UserMapper;
import hcmute.lp.backend.repository.UserRepository;
import hcmute.lp.backend.security.CustomUserDetails;
import hcmute.lp.backend.security.JwtService;
import hcmute.lp.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

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

            // Tạo token JWT
            String accessToken = jwtService.generateToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

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
}

