package hcmute.lp.backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Column(name = "address")
    private String address;

    @Column(name = "membership_level")
    private String membershipLevel;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "loyalty_points")
    private Integer loyaltyPoints = 0;

    public enum GenderType {
        MALE, FEMALE, OTHER
    }
}
