package hcmute.lp.backend.model.entity;


import hcmute.lp.backend.model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany
    @JoinColumn(name = "role_id")
    private List<User> user;
}
