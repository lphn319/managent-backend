package hcmute.lp.backend.repository;

import hcmute.lp.backend.model.common.CommonCategories;
import hcmute.lp.backend.model.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    boolean existsByName(String name);
}