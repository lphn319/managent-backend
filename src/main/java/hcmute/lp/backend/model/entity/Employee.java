package hcmute.lp.backend.model.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees")
public class Employee extends User{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;

    @Column(name = "role")
    private String role;

    @OneToMany
    @JoinColumn(name = "employee_id")
    private List<Order> orders;

    @OneToMany
    @JoinColumn(name = "employee_id")
    private List<Import> imports;

}
