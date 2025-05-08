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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "imports")
public class Import extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Thay đổi từ int sang Long

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @OneToMany(mappedBy = "importOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImportDetail> importDetails;

    //Helper method to check the value of status
    public boolean isProcessing() {
        return CommonCategories.ImportStatus.PROCESSING.equals(this.status);
    }

    public boolean isCompleted() {
        return CommonCategories.ImportStatus.COMPLETED.equals(this.status);
    }

    public boolean isCancelled() {
        return CommonCategories.ImportStatus.CANCELLED.equals(this.status);
    }
}