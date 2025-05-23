// User.java
package hcmute.lp.backend.model.entity;

import hcmute.lp.backend.model.common.BaseEntity;
import hcmute.lp.backend.model.common.CommonCategories;
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

    @Column(name= "password", nullable = false)
    private String password;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> additionalPermissions = new HashSet<>();



    // Helper method để kiểm tra quyền
    public boolean hasPermission(String permissionCode) {
        // Check permissions from the role
        if (role != null && role.hasPermission(permissionCode)) {
            return true;
        }

        // Check additional permissions
        return additionalPermissions.stream()
                .anyMatch(p -> p.getCode().equals(permissionCode));
    }

    public Set<Permission> getAllPermissions() {
        Set<Permission> allPermissions = new HashSet<>();

        // Add permissions from the role
        if (role != null) {
            allPermissions.addAll(role.getPermissions());
        }

        // Add additional permissions
        allPermissions.addAll(additionalPermissions);

        return allPermissions;
    }

    // Helper method to check the value of status
    public boolean isActive() {
        return CommonCategories.UserStatus.ACTIVE.equals(this.status);
    }
    public boolean isInactive() {
        return CommonCategories.UserStatus.INACTIVE.equals(this.status);
    }
}