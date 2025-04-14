package hcmute.lp.backend.repository;

import hcmute.lp.backend.model.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    Supplier findByCompanyName(String companyName);
    boolean existsByCompanyName(String companyName);
    boolean existsById(Integer id);
}
