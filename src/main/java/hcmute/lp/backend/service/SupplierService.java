package hcmute.lp.backend.service;

import hcmute.lp.backend.model.dto.supplier.SupplierDto;
import hcmute.lp.backend.model.dto.supplier.SupplierRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface SupplierService {
    List<SupplierDto> getAllSuppliers();
    SupplierDto getSupplierById(int id);
    SupplierDto createSupplier(SupplierRequest supplierRequest);
    SupplierDto updateSupplier(int id, SupplierRequest supplierRequest);
    void deleteSupplier(int id);
    boolean existsById(int id);
    boolean existsByCompanyName(String companyName);

    // Thêm phương thức cập nhật trạng thái
    SupplierDto updateSupplierStatus(int id, String status);

    // Thêm phương thức cập nhật danh mục
    SupplierDto updateSupplierCategories(int id, Set<Integer> categoryIds);

    // Thêm phương thức phân trang
    Page<SupplierDto> getSuppliersPaginated(
            int page, int size, String sortBy, String sortDirection, String keyword);
}