package hcmute.lp.backend.service;

import hcmute.lp.backend.model.dto.supplier.SupplierDto;
import hcmute.lp.backend.model.dto.supplier.SupplierRequest;

import java.util.List;

public interface SupplierService {
    List<SupplierDto> getAllSuppliers();
    SupplierDto getSupplierById(int id);
    SupplierDto createSupplier(SupplierRequest supplierRequest);
    SupplierDto updateSupplier(int id, SupplierRequest supplierRequest);
    void deleteSupplier(int id);
    boolean existsById(int id);
    boolean existsByCompanyName(String name);
}
