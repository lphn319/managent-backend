package hcmute.lp.backend.service.impl;

import hcmute.lp.backend.exception.ResourceNotFoundException;
import hcmute.lp.backend.model.dto.supplier.SupplierDto;
import hcmute.lp.backend.model.dto.supplier.SupplierRequest;
import hcmute.lp.backend.model.entity.Supplier;
import hcmute.lp.backend.model.mapper.SupplierMapper;
import hcmute.lp.backend.repository.SupplierRepository;
import hcmute.lp.backend.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplierMapper supplierMapper;

    @Override
    public List<SupplierDto> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return supplierMapper.toDtoList(suppliers);
    }

    @Override
    public SupplierDto getSupplierById(int id) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Supplier not found with id: " + id));
        return supplierMapper.toDto(supplier);
    }

    @Override
    @Transactional
    public SupplierDto createSupplier(SupplierRequest supplierRequest) {
        if (supplierRepository.existsByCompanyName(supplierRequest.getCompanyName())) {
            throw new IllegalArgumentException("Supplier already exists with name: " + supplierRequest.getCompanyName());
        }
        Supplier supplier = supplierMapper.toEntity(supplierRequest);
        Supplier savedSupplier = supplierRepository.save(supplier);
        return supplierMapper.toDto(savedSupplier);
    }

    @Override
    public SupplierDto updateSupplier(int id, SupplierRequest supplierRequest) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Supplier not found with id: " + id));

        if (!supplier.getCompanyName().equals(supplierRequest.getCompanyName()) && supplierRepository.existsByCompanyName(supplierRequest.getCompanyName())) {
            throw new IllegalArgumentException("Supplier with this name already exists: " + supplierRequest.getCompanyName());
        }
        supplierMapper.updateEntityFromRequest(supplier, supplierRequest);
        Supplier updatedSupplier = supplierRepository.save(supplier);
        return supplierMapper.toDto(updatedSupplier);
    }

    @Override
    public void deleteSupplier(int id) {
        if (!supplierRepository.existsById(id)) {
            throw new ResourceNotFoundException("Supplier not found with id: " + id);
        }
        supplierRepository.deleteById(id);
    }

    @Override
    public boolean existsById(int id) {
        return supplierRepository.existsById(id);
    }

    @Override
    public boolean existsByCompanyName(String companyName) {
        return supplierRepository.existsByCompanyName(companyName);
    }
}
