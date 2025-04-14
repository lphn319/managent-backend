package hcmute.lp.backend.repository;

import hcmute.lp.backend.model.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    Brand findByName(String name);

    boolean existsByName(String name);

    boolean existsById(Integer id);
}