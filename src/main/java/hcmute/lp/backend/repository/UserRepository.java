package hcmute.lp.backend.repository;

import hcmute.lp.backend.model.common.CommonCategories;
import hcmute.lp.backend.model.entity.Department;
import hcmute.lp.backend.model.entity.Role;
import hcmute.lp.backend.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.id IN " +
            "(SELECT c.id FROM Customer c WHERE c.phoneNumber = :phone) OR " +
            "u.id IN (SELECT e.id FROM Employee e WHERE e.phoneNumber = :phone)")
    boolean existsByPhoneNumber(String phone);

    Optional<User> findByEmail(String email);
}