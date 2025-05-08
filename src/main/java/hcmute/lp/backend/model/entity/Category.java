package hcmute.lp.backend.model.entity;

import hcmute.lp.backend.model.common.BaseEntity;
import hcmute.lp.backend.model.common.CommonCategories;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Category> children;

    @ManyToMany(mappedBy = "categories", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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

    // Helper method to check if the category is a parent category
    public boolean isParentCategory() {
        return this.parent == null;
    }
}
