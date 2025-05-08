package hcmute.lp.backend.repository;

import hcmute.lp.backend.model.common.CommonCategories;
import hcmute.lp.backend.model.entity.Import;
import hcmute.lp.backend.model.entity.ImportDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportRepository extends JpaRepository<Import, Long> { // Thay đổi từ Integer sang Long

}