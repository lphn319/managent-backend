package hcmute.lp.backend.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends User{
    @Column(name = "point", nullable = false)
    private int point = 0;

    @Column(name = "address", nullable = false)
    private String address;

    @OneToMany
    @jakarta.persistence.JoinColumn(name = "customer_id")
    private List<Order> orders;
}
