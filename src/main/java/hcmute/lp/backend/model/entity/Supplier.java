package hcmute.lp.backend.model.entity;

import hcmute.lp.backend.model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

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

    @Column(name ="phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "description")
    private String description;

    @OneToMany
    @JoinColumn(name = "supplier_id")
    private List<Import> imports;

}
