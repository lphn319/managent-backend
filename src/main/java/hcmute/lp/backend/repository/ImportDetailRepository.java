package hcmute.lp.backend.repository;

import hcmute.lp.backend.model.entity.ImportDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportDetailRepository extends JpaRepository<ImportDetail, Long> { // Thay đổi từ Integer sang Long
    List<ImportDetail> findByImportOrderId(Long importId); // Thay đổi từ int sang Long
}