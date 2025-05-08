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
@Table(name = "customers")
public class Customer {
    @Id
    private long id; // Cùng ID với User

    @OneToOne
    @MapsId // Sử dụng cùng ID với User
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "address")
    private String address;

    @Column(name = "membership_level")
    private String membershipLevel;

    @Column(name = "loyalty_points")
    private Integer loyaltyPoints = 0;

    //Helper method to check the value of gender
    public boolean isMale() {
        return CommonCategories.GenderType.MALE.equals(this.gender);
    }

    public boolean isFemale() {
        return CommonCategories.GenderType.FEMALE.equals(this.gender);
    }

    public boolean isOther() {
        return CommonCategories.GenderType.OTHER.equals(this.gender);
    }
}
