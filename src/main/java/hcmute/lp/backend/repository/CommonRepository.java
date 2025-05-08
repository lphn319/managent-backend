package hcmute.lp.backend.repository;

import hcmute.lp.backend.model.common.Common;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommonRepository extends JpaRepository<Common, Integer> {
    List<Common> findByCategory(String category);
    Optional<Common> findByCategoryAndValue(String category, String value);
}



