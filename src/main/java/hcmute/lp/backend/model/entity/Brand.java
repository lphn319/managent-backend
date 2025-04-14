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
@Table(name = "brands", uniqueConstraints = {
        @jakarta.persistence.UniqueConstraint(columnNames = "name")
})
public class Brand extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;

    @jakarta.persistence.Column(name = "name", nullable = false)
    private String name;

    @jakarta.persistence.Column(name = "description")
    private String description;

    @OneToMany
    @JoinColumn(name = "brand_id")
    private List<Product> product;
}
