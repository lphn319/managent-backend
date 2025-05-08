package hcmute.lp.backend.model.entity;

import hcmute.lp.backend.model.common.CommonCategories;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    // Helper method
    public boolean hasPermission(String permissionCode) {
        return permissions.stream()
                .anyMatch(p -> p.getCode().equals(permissionCode));
    }

    // Helper method to check the Role
    public boolean isAdmin() {
        return CommonCategories.RoleType.ADMIN.equals( this.name );
    }
    public boolean isEmployee() {
        return CommonCategories.RoleType.EMPLOYEE.equals( this.name );
    }
    public boolean isCustomer() {
        return CommonCategories.RoleType.CUSTOMER.equals( this.name );
    }
    public boolean isManager() {
        return CommonCategories.RoleType.MANAGER.equals( this.name );
    }

}
