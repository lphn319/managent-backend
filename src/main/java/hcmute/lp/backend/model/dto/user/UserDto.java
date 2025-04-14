package hcmute.lp.backend.model.dto.user;

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
    private long id;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private boolean isActive;
    private String roleName;
    private String departmentName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}