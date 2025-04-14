package hcmute.lp.backend.repository;

import hcmute.lp.backend.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByName(String name);

    boolean existsById(Integer id);

    Product findByName(String name);
}
