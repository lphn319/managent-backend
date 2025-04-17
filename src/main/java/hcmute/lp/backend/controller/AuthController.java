package hcmute.lp.backend.controller;

import hcmute.lp.backend.model.common.ApiResponse;
import hcmute.lp.backend.model.dto.auth.AuthResponse;
import hcmute.lp.backend.model.dto.auth.LoginRequest;
import hcmute.lp.backend.model.dto.auth.RegisterRequest;
import hcmute.lp.backend.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Login successful")
                .data(authResponse)
                .build());
    }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest registerRequest) throws BadRequestException {
        AuthResponse authResponse = authService.register(registerRequest);
        return new ResponseEntity<>(new ApiResponse(true, "Đăng ký thành công", authResponse), HttpStatus.CREATED);
    }
}
