package hcmute.lp.backend.repository;

import hcmute.lp.backend.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByName(String name);

    boolean existsByName(String name);

    boolean existsById(Integer id);
}
