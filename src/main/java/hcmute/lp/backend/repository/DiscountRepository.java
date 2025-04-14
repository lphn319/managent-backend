package hcmute.lp.backend.repository;

import hcmute.lp.backend.model.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    boolean existsByCode(String code);

    boolean existsByName(String name);

    Discount findByCode(String code);
}
