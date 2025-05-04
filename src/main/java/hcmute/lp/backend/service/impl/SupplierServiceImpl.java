package hcmute.lp.backend.service.impl;

import hcmute.lp.backend.exception.ResourceNotFoundException;
import hcmute.lp.backend.model.dto.supplier.SupplierDto;
import hcmute.lp.backend.model.dto.supplier.SupplierRequest;
import hcmute.lp.backend.model.entity.Category;
import hcmute.lp.backend.model.entity.Supplier;
import hcmute.lp.backend.model.mapper.SupplierMapper;
import hcmute.lp.backend.repository.CategoryRepository;
import hcmute.lp.backend.repository.SupplierRepository;
import hcmute.lp.backend.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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

        // Đặt giá trị mặc định cho status nếu chưa có
        if (supplier.getStatus() == null) {
            supplier.setStatus(Supplier.SupplierStatus.ACTIVE);
        }

        Supplier savedSupplier = supplierRepository.save(supplier);
        return supplierMapper.toDto(savedSupplier);
    }

    @Override
    @Transactional
    public SupplierDto updateSupplier(int id, SupplierRequest supplierRequest) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Supplier not found with id: " + id));

        if (!supplier.getCompanyName().equals(supplierRequest.getCompanyName()) &&
                supplierRequest.getCompanyName() != null &&
                supplierRepository.existsByCompanyName(supplierRequest.getCompanyName())) {
            throw new IllegalArgumentException("Supplier with this name already exists: " + supplierRequest.getCompanyName());
        }

        supplierMapper.updateEntityFromRequest(supplier, supplierRequest);
        Supplier updatedSupplier = supplierRepository.save(supplier);
        return supplierMapper.toDto(updatedSupplier);
    }

    @Override
    @Transactional
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

    @Override
    public SupplierDto updateSupplierStatus(int id, String status) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Supplier not found with id: " + id));

        try {
            supplier.setStatus(Supplier.SupplierStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }

        Supplier updatedSupplier = supplierRepository.save(supplier);
        return supplierMapper.toDto(updatedSupplier);
    }

    @Override
    @Transactional
    public SupplierDto updateSupplierCategories(int id, Set<Integer> categoryIds) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Supplier not found with id: " + id));

        Set<Category> categories = new HashSet<>();
        if (categoryIds != null && !categoryIds.isEmpty()) {
            categories = categoryIds.stream()
                    .map(categoryId -> categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId)))
                    .collect(Collectors.toSet());
        }

        supplier.setCategories(categories);
        Supplier updatedSupplier = supplierRepository.save(supplier);
        return supplierMapper.toDto(updatedSupplier);
    }

    @Override
    public Page<SupplierDto> getSuppliersPaginated(int page, int size, String sortBy, String sortDirection, String keyword) {
        // Tạo đối tượng Pageable với thông số phân trang
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Supplier> supplierPage;

        // Nếu có từ khóa tìm kiếm
        if (keyword != null && !keyword.isEmpty()) {
            supplierPage = supplierRepository.findByCompanyNameContaining(keyword, pageable);
        } else {
            // Nếu không có từ khóa, trả về tất cả
            supplierPage = supplierRepository.findAll(pageable);
        }

        // Chuyển đổi Page<Supplier> thành Page<SupplierDto>
        return supplierPage.map(supplierMapper::toDto);
    }
}