package hcmute.lp.backend.model.entity;

import hcmute.lp.backend.model.common.CommonCategories;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    private long id; // Cùng ID với User

    @OneToOne
    @MapsId // Sử dụng cùng ID với User
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "employee_id", unique = true)
    private String employeeId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    private String gender;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    //Helper method to check the gender value
    public boolean isMale() {
        return CommonCategories.GenderType.MALE.equals(this.gender);
    }

    public boolean isFemale() {
        return CommonCategories.GenderType.FEMALE.equals(this.gender);
    }
}
