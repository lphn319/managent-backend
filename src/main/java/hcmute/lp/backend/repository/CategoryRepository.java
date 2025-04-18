package hcmute.lp.backend.repository;

import hcmute.lp.backend.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByName(String name);

    boolean existsByName(String name);

    boolean existsById(Integer id);

    List<Category> findByParentId(Integer parentId);
    List<Category> findByParentIdIsNull();
    List<Category> findByParentIdIsNotNull();
    List<Category> findByParentIdAndStatus(Integer parentId, String status);
    List<Category> findByParentIdIsNullAndStatus(String status);
    List<Category> findByParentIdIsNotNullAndStatus(String status);

    List<Category> findByStatus(String status);

}
