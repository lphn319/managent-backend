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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "imports")
public class Import extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Thay đổi từ int sang Long

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ImportStatus status;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User employee;

    @OneToMany(mappedBy = "importOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImportDetail> importDetails;

    public enum ImportStatus {
        PROCESSING("processing"),
        COMPLETED("completed"),
        CANCELLED("cancelled");

        private final String value;

        ImportStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static ImportStatus fromValue(String value) {
            for (ImportStatus status : ImportStatus.values()) {
                if (status.getValue().equals(value)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid ImportStatus value: " + value);
        }
    }
}