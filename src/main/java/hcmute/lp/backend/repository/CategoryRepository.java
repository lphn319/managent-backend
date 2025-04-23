package hcmute.lp.backend.repository;

import hcmute.lp.backend.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByName(String name);

    boolean existsByName(String name);

    boolean existsById(Integer id);

    Page<Category> findByStatus(Category.CategoryStatus status, Pageable pageable);
}
