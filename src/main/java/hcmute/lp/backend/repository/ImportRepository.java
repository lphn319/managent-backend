package hcmute.lp.backend.repository;

import hcmute.lp.backend.model.entity.Import;
import hcmute.lp.backend.model.entity.Import.ImportStatus;
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
    Page<Import> findByStatus(ImportStatus status, Pageable pageable);

    // Tìm kiếm theo supplier
    @Query("SELECT i FROM Import i WHERE i.supplier.id = :supplierId")
    Page<Import> findBySupplier(@Param("supplierId") Long supplierId, Pageable pageable);

    // Tìm kiếm theo employee
    @Query("SELECT i FROM Import i WHERE i.employee.id = :employeeId")
    Page<Import> findByEmployee(@Param("employeeId") Long employeeId, Pageable pageable);

    // Tìm kiếm kết hợp theo supplier và status
    @Query("SELECT i FROM Import i WHERE i.supplier.id = :supplierId AND i.status = :status")
    Page<Import> findBySupplierAndStatus(@Param("supplierId") Long supplierId, @Param("status") ImportStatus status, Pageable pageable);

    // Tìm kiếm kết hợp theo employee và status
    @Query("SELECT i FROM Import i WHERE i.employee.id = :employeeId AND i.status = :status")
    Page<Import> findByEmployeeAndStatus(@Param("employeeId") Long employeeId, @Param("status") ImportStatus status, Pageable pageable);

    // Tìm kiếm kết hợp theo supplier và employee
    @Query("SELECT i FROM Import i WHERE i.supplier.id = :supplierId AND i.employee.id = :employeeId")
    Page<Import> findBySupplierAndEmployee(@Param("supplierId") Long supplierId, @Param("employeeId") Long employeeId, Pageable pageable);

    // Tìm kiếm kết hợp theo cả 3 tiêu chí
    @Query("SELECT i FROM Import i WHERE i.supplier.id = :supplierId AND i.employee.id = :employeeId AND i.status = :status")
    Page<Import> findBySupplierAndEmployeeAndStatus(
            @Param("supplierId") Long supplierId,
            @Param("employeeId") Long employeeId,
            @Param("status") ImportStatus status,
            Pageable pageable);

    // Tìm chi tiết nhập hàng theo id
    @Query("SELECT i.importDetails FROM Import i WHERE i.id = :importId")
    List<ImportDetail> findDetailsByImportId(@Param("importId") Long importId);
}