package hcmute.lp.backend.model.entity;

import hcmute.lp.backend.model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id", nullable = true)
    private Discount discount;

    // Thêm vào entity Order
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;

    // Thêm helper method để tính tổng tiền
    public double calculateTotalAmount() {
        if (orderDetails == null || orderDetails.isEmpty()) {
            return 0;
        }

        double total = orderDetails.stream()
                .mapToDouble(OrderDetail::getSubtotal)
                .sum();

        // Áp dụng discount nếu có
        if (discount != null) {
            total = total * (1 - discount.getDiscountRate());
        }

        return total;
    }

    // Thêm helper method để cập nhật tổng tiền
    @PrePersist
    @PreUpdate
    public void updateTotalAmount() {
        this.totalAmount = calculateTotalAmount();
    }
}
