// UserDto.java
package hcmute.lp.backend.model.dto.user;

import hcmute.lp.backend.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private User.UserStatus status;
    private String roleName;
    private String departmentName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Helper method for backward compatibility
    public boolean isActive() {
        return this.status == User.UserStatus.ACTIVE;
    }
}