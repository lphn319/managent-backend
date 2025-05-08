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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "origin")
    private String origin;

    @Column(name = "website")
    private String website;

    @OneToMany
    @JoinColumn(name = "brand_id")
    private List<Product> products;

    // Transient field for product count (not stored in a database)
    @Transient
    private Integer productCount;

    // Helper method to get product count
    public Integer getProductCount() {
        if (this.productCount != null) {
            return this.productCount;
        }
        return this.products != null ? this.products.size() : 0;
    }
}
