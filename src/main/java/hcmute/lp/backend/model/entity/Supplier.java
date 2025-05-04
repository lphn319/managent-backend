package hcmute.lp.backend.model.entity;

import hcmute.lp.backend.model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "suppliers", uniqueConstraints = {
        @UniqueConstraint(columnNames = "company_name")
})
public class Supplier extends BaseEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "address")
    private String address;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "description")
    private String description;

    @Column(name = "logo")
    private String logo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SupplierStatus status = SupplierStatus.ACTIVE;

    @OneToMany(mappedBy = "supplier")
    private List<Import> imports;

    @ManyToMany
    @JoinTable(
            name = "supplier_categories",
            joinColumns = @JoinColumn(name = "supplier_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    public enum SupplierStatus {
        ACTIVE, INACTIVE
    }

    // Các helper methods để quản lý quan hệ
    public void addCategory(Category category) {
        this.categories.add(category);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
    }
}