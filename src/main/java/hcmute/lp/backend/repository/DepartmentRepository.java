package hcmute.lp.backend.repository;

import hcmute.lp.backend.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    boolean existsByName(String name);

    boolean existsByIdAndName(Integer id, String name);

    Optional<Department> findByName(String name);
}
