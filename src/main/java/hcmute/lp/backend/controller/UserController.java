package hcmute.lp.backend.controller;

import hcmute.lp.backend.model.common.ApiResponse;
import hcmute.lp.backend.model.dto.user.UserDto;
import hcmute.lp.backend.model.dto.user.UserRequest;
import hcmute.lp.backend.security.annotation.HasAnyRole;
import hcmute.lp.backend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "USER API")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getCurrentUser() {
        UserDto userDto = userService.getCurrentUser();
        return ResponseEntity.ok(new ApiResponse(true, "Lấy thông tin người dùng hiện tại thành công", userDto));
    }

    @GetMapping
    @HasAnyRole({"ADMIN"})
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse(true, "Lấy danh sách người dùng thành công", users));
    }

    @GetMapping("/{id}")
    @HasAnyRole({"ADMIN"})
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(new ApiResponse(true, "Lấy thông tin người dùng thành công", userDto));
    }

    @PostMapping
//    @HasAnyRole({"ADMIN"})
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        UserDto createdUser = userService.createUser(userRequest);
        return new ResponseEntity<>(new ApiResponse(true, "Tạo người dùng thành công", createdUser), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
        UserDto updatedUser = userService.updateUser(id, userRequest);
        return ResponseEntity.ok(new ApiResponse(true, "Cập nhật thông tin người dùng thành công", updatedUser));
    }

    @DeleteMapping("/{id}")
    @HasAnyRole({"ADMIN"})
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse(true, "Xóa người dùng thành công", null));
    }

    @PatchMapping("/{id}/status")
    @HasAnyRole({"ADMIN"})
    public ResponseEntity<ApiResponse> changeUserStatus(@PathVariable Long id, @RequestParam boolean isActive) {
        userService.changeUserStatus(id, isActive);
        String message = isActive ? "Kích hoạt tài khoản thành công" : "Vô hiệu hóa tài khoản thành công";
        return ResponseEntity.ok(new ApiResponse(true, message, null));
    }
}