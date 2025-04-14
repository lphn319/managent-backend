package hcmute.lp.backend.repository;

import hcmute.lp.backend.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    boolean existsByName(String name);

    boolean existsByIdAndName(Integer id, String name);

    Department findByName(String name);
}
