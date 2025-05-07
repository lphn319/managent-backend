// User.java
package hcmute.lp.backend.model.entity;

import hcmute.lp.backend.model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"}),
        @UniqueConstraint(columnNames = {"phone_number"})
})
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;


    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE; // Default value

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> additionalPermissions = new HashSet<>();

    public enum UserStatus {
        ACTIVE, INACTIVE
    }

    // Helper method for backward compatibility
    @Transient
    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }

    // Helper method for backward compatibility
    public void setActive(boolean active) {
        this.status = active ? UserStatus.ACTIVE : UserStatus.INACTIVE;
    }

    // Helper method để kiểm tra quyền
    public boolean hasPermission(String permissionCode) {
        // Kiểm tra quyền từ tất cả các role
        for (Role role : roles) {
            if (role.hasPermission(permissionCode)) {
                return true;
            }
        }

        // Kiểm tra quyền bổ sung
        return additionalPermissions.stream()
                .anyMatch(p -> p.getCode().equals(permissionCode));
    }

    // Helper method để lấy tất cả quyền
    public Set<Permission> getAllPermissions() {
        Set<Permission> allPermissions = new HashSet<>();

        // Thêm quyền từ tất cả các role
        for (Role role : roles) {
            allPermissions.addAll(role.getPermissions());
        }

        // Thêm quyền bổ sung
        allPermissions.addAll(additionalPermissions);

        return allPermissions;
    }
}