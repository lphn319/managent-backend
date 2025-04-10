package hcmute.lp.backend.model.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Admin extends User {
    // Add any additional fields or methods specific to Admin here
    // For example, you might want to add admin-specific permissions or roles
    // private String role;
}
