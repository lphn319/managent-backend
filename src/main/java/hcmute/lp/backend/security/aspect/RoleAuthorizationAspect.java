package hcmute.lp.backend.security.aspect;

import hcmute.lp.backend.exception.UnauthorizedException;
import hcmute.lp.backend.security.annotation.HasAnyRole;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

@Aspect
@Component
public class RoleAuthorizationAspect {

    @Around("@annotation(hcmute.lp.backend.security.annotation.HasAnyRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        HasAnyRole hasAnyRole = signature.getMethod().getAnnotation(HasAnyRole.class);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Người dùng chưa đăng nhập");
        }

        Collection<String> authorities = authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .toList();

        boolean hasRequiredRole = authorities.stream()
                .anyMatch(authority -> Arrays.stream(hasAnyRole.value())
                        .anyMatch(authority::equals));

        if (!hasRequiredRole) {
            throw new UnauthorizedException("Người dùng không có quyền truy cập");
        }

        return joinPoint.proceed();
    }
}