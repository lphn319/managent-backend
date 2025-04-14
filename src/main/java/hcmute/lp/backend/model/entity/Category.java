package hcmute.lp.backend.model.entity;

import hcmute.lp.backend.model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "categories", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;
}
