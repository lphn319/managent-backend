package hcmute.lp.backend.security;

import hcmute.lp.backend.exception.UnauthorizedException;
import hcmute.lp.backend.model.entity.User;
import hcmute.lp.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityUtils {

    private final UserRepository userRepository;

    /**
     * Lấy thông tin người dùng hiện tại
     * @return Thông tin người dùng
     * @throws UnauthorizedException nếu người dùng chưa đăng nhập
     */
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Người dùng chưa đăng nhập");
        }

        String email;
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            email = userDetails.getUsername();
        } else {
            email = principal.toString();
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Không tìm thấy người dùng"));
    }

    /**
     * Kiểm tra người dùng hiện tại có vai trò cụ thể không
     * @param roleName Tên vai trò cần kiểm tra
     * @return true nếu có vai trò, false nếu không
     */
    public boolean hasRole(String roleName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
                authentication.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + roleName));
    }

    /**
     * Kiểm tra người dùng hiện tại có một trong các vai trò được chỉ định không
     * @param roleNames Danh sách vai trò cần kiểm tra
     * @return true nếu có ít nhất một vai trò, false nếu không
     */
    public boolean hasAnyRole(String... roleNames) {
        for (String roleName : roleNames) {
            if (hasRole(roleName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Kiểm tra người dùng có quyền chỉnh sửa tài nguyên
     * @param resourceOwnerId ID của chủ sở hữu tài nguyên
     * @return true nếu là chủ sở hữu hoặc admin
     */
    public boolean canModifyResource(long resourceOwnerId) {
        try {
            User currentUser = getCurrentUser();
            return currentUser.getId() == resourceOwnerId ||
                    "ADMIN".equals(currentUser.getRole().getName());
        } catch (UnauthorizedException e) {
            log.warn("Unauthorized user attempted to modify resource: {}", e.getMessage());
            return false;
        }
    }
}