package hcmute.lp.backend.service.impl;

import hcmute.lp.backend.exception.ResourceNotFoundException;
import hcmute.lp.backend.model.dto.import_.ImportDto;
import hcmute.lp.backend.model.dto.import_.ImportRequest;
import hcmute.lp.backend.model.entity.Import;
import hcmute.lp.backend.model.entity.Import.ImportStatus;
import hcmute.lp.backend.model.entity.ImportDetail;
import hcmute.lp.backend.model.entity.Product;
import hcmute.lp.backend.model.entity.Supplier;
import hcmute.lp.backend.model.entity.User;
import hcmute.lp.backend.model.mapper.ImportMapper;
import hcmute.lp.backend.repository.ImportDetailRepository;
import hcmute.lp.backend.repository.ImportRepository;
import hcmute.lp.backend.repository.ProductRepository;
import hcmute.lp.backend.repository.SupplierRepository;
import hcmute.lp.backend.repository.UserRepository;
import hcmute.lp.backend.service.ImportService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImportServiceImpl implements ImportService {
    @Autowired
    private ImportRepository importRepository;

    @Autowired
    private ImportDetailRepository importDetailRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImportMapper importMapper;

    @Override
    public List<ImportDto> getAllImports() {
        List<Import> imports = importRepository.findAll();
        return importMapper.toDtoList(imports);
    }

    @Override
    public Page<ImportDto> getImportsPaginated(int page, int size, String sortBy, String sortDirection,
                                               Long supplierId, Long employeeId, String status) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Import> importsPage;

        // Chuyển đổi status từ String sang enum
        ImportStatus importStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                importStatus = ImportStatus.fromValue(status);
            } catch (IllegalArgumentException e) {
                // Nếu status không hợp lệ, bỏ qua tham số này
            }
        }

        // Xử lý các trường hợp lọc khác nhau
        if (supplierId != null && employeeId != null && importStatus != null) {
            // Lọc theo cả 3 tiêu chí
            importsPage = importRepository.findBySupplierAndEmployeeAndStatus(supplierId, employeeId, importStatus, pageable);
        } else if (supplierId != null && employeeId != null) {
            // Lọc theo supplier và employee
            importsPage = importRepository.findBySupplierAndEmployee(supplierId, employeeId, pageable);
        } else if (supplierId != null && importStatus != null) {
            // Lọc theo supplier và status
            importsPage = importRepository.findBySupplierAndStatus(supplierId, importStatus, pageable);
        } else if (employeeId != null && importStatus != null) {
            // Lọc theo employee và status
            importsPage = importRepository.findByEmployeeAndStatus(employeeId, importStatus, pageable);
        } else if (supplierId != null) {
            // Lọc chỉ theo supplier
            importsPage = importRepository.findBySupplier(supplierId, pageable);
        } else if (employeeId != null) {
            // Lọc chỉ theo employee
            importsPage = importRepository.findByEmployee(employeeId, pageable);
        } else if (importStatus != null) {
            // Lọc chỉ theo status
            importsPage = importRepository.findByStatus(importStatus, pageable);
        } else {
            // Không có điều kiện lọc
            importsPage = importRepository.findAll(pageable);
        }

        return importsPage.map(importMapper::toDto);
    }

    @Override
    public ImportDto getImportById(Long id) {
        Import importEntity = importRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Import not found with id: " + id));
        return importMapper.toDto(importEntity);
    }

    @Override
    @Transactional
    public ImportDto createImport(ImportRequest importRequest) {
        // Map request to entity
        Import importEntity = importMapper.toEntity(importRequest);

        // Set status
        if (importRequest.getStatus() != null && !importRequest.getStatus().isEmpty()) {
            try {
                importEntity.setStatus(ImportStatus.fromValue(importRequest.getStatus()));
            } catch (IllegalArgumentException e) {
                // Default to PROCESSING if status is invalid
                importEntity.setStatus(ImportStatus.PROCESSING);
            }
        } else {
            // Default status for new imports
            importEntity.setStatus(ImportStatus.PROCESSING);
        }

        // Handle supplier
        Supplier supplier = supplierRepository.findById(importRequest.getSupplierId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + importRequest.getSupplierId()));
        importEntity.setSupplier(supplier);

        // Handle employee
        User employee = userRepository.findById((long)importRequest.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + importRequest.getEmployeeId()));
        importEntity.setEmployee(employee);

        // Calculate total quantity and amount
        int totalQuantity = 0;
        double totalAmount = 0;

        // Save the import first to get the ID
        Import savedImport = importRepository.save(importEntity);

        // Process import details
        List<ImportDetail> importDetails = new ArrayList<>();

        for (var detailRequest : importRequest.getImportDetails()) {
            ImportDetail detail = importMapper.toDetailEntity(detailRequest);

            // Set import reference
            detail.setImportOrder(savedImport);

            // Handle product
            Product product = productRepository.findById(detailRequest.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + detailRequest.getProductId()));
            detail.setProduct(product);

            // Update totals
            totalQuantity += detailRequest.getQuantity();
            totalAmount += detailRequest.getQuantity() * detailRequest.getPrice();

            importDetails.add(detail);

            // Update product quantity only if status is not CANCELLED
            if (savedImport.getStatus() != ImportStatus.CANCELLED) {
                product.setQuantity(product.getQuantity() + detailRequest.getQuantity());
                productRepository.save(product);
            }
        }

        // Save all details
        importDetailRepository.saveAll(importDetails);

        // Update import with totals
        savedImport.setQuantity(totalQuantity);
        savedImport.setTotalAmount(totalAmount);
        savedImport.setImportDetails(importDetails);

        // Save updated import
        Import finalSavedImport = importRepository.save(savedImport);

        return importMapper.toDto(finalSavedImport);
    }

    @Override
    @Transactional
    public ImportDto updateImport(Long id, ImportRequest importRequest) {
        Import importEntity = importRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Import not found with id: " + id));

        // Only allow update if status is PROCESSING
        if (importEntity.getStatus() != ImportStatus.PROCESSING) {
            throw new IllegalArgumentException("Cannot update import that is not in processing status");
        }

        // Update status if provided
        if (importRequest.getStatus() != null && !importRequest.getStatus().isEmpty()) {
            try {
                importEntity.setStatus(ImportStatus.fromValue(importRequest.getStatus()));
            } catch (IllegalArgumentException e) {
                // Keep existing status if new one is invalid
            }
        }

        // Update notes
        importEntity.setNotes(importRequest.getNotes());

        // Handle supplier
        if (importEntity.getSupplier().getId() != importRequest.getSupplierId()) {
            Supplier supplier = supplierRepository.findById(importRequest.getSupplierId())
                    .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + importRequest.getSupplierId()));
            importEntity.setSupplier(supplier);
        }

        // Handle employee
        if (importEntity.getEmployee().getId() != importRequest.getEmployeeId()) {
            User employee = userRepository.findById((long)importRequest.getEmployeeId())
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + importRequest.getEmployeeId()));
            importEntity.setEmployee(employee);
        }

        // Remove old details and revert product quantities
        List<ImportDetail> oldDetails = importDetailRepository.findByImportOrderId(id);
        for (ImportDetail detail : oldDetails) {
            Product product = detail.getProduct();
            // Deduct the previously added quantity only if status was not CANCELLED
            if (importEntity.getStatus() != ImportStatus.CANCELLED) {
                product.setQuantity(product.getQuantity() - detail.getQuantity());
                productRepository.save(product);
            }
        }
        importDetailRepository.deleteAll(oldDetails);

        // Process new details
        List<ImportDetail> newDetails = new ArrayList<>();
        int totalQuantity = 0;
        double totalAmount = 0;

        for (var detailRequest : importRequest.getImportDetails()) {
            ImportDetail detail = importMapper.toDetailEntity(detailRequest);

            // Set import reference
            detail.setImportOrder(importEntity);

            // Handle product
            Product product = productRepository.findById(detailRequest.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + detailRequest.getProductId()));
            detail.setProduct(product);

            // Update totals
            totalQuantity += detailRequest.getQuantity();
            totalAmount += detailRequest.getQuantity() * detailRequest.getPrice();

            newDetails.add(detail);

            // Update product quantity only if new status is not CANCELLED
            ImportStatus newStatus = importEntity.getStatus();
            if (newStatus != ImportStatus.CANCELLED) {
                product.setQuantity(product.getQuantity() + detailRequest.getQuantity());
                productRepository.save(product);
            }
        }

        // Save all details
        importDetailRepository.saveAll(newDetails);

        // Update import with totals
        importEntity.setQuantity(totalQuantity);
        importEntity.setTotalAmount(totalAmount);
        importEntity.setImportDetails(newDetails);

        // Save updated import
        Import updatedImport = importRepository.save(importEntity);

        return importMapper.toDto(updatedImport);
    }

    @Override
    @Transactional
    public ImportDto updateStatus(Long id, String statusValue) {
        Import importEntity = importRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Import not found with id: " + id));

        // Validate and convert status
        ImportStatus newStatus;
        try {
            newStatus = ImportStatus.fromValue(statusValue);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value. Allowed values: processing, completed, cancelled");
        }

        ImportStatus oldStatus = importEntity.getStatus();

        // Handle product quantity adjustments if status is changing to or from CANCELLED
        if (newStatus == ImportStatus.CANCELLED && oldStatus != ImportStatus.CANCELLED) {
            // Cancel: revert product quantities
            for (ImportDetail detail : importEntity.getImportDetails()) {
                Product product = detail.getProduct();
                product.setQuantity(product.getQuantity() - detail.getQuantity());
                productRepository.save(product);
            }
        } else if (newStatus != ImportStatus.CANCELLED && oldStatus == ImportStatus.CANCELLED) {
            // Un-cancel: add quantities back
            for (ImportDetail detail : importEntity.getImportDetails()) {
                Product product = detail.getProduct();
                product.setQuantity(product.getQuantity() + detail.getQuantity());
                productRepository.save(product);
            }
        }

        // Update status
        importEntity.setStatus(newStatus);
        Import updatedImport = importRepository.save(importEntity);

        return importMapper.toDto(updatedImport);
    }

    @Override
    @Transactional
    public void deleteImport(Long id) {
        Import importEntity = importRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Import not found with id: " + id));

        // Only allow deletion if status is PROCESSING
        if (importEntity.getStatus() != ImportStatus.PROCESSING) {
            throw new IllegalArgumentException("Cannot delete import that is not in processing status");
        }

        // Revert product quantities
        for (ImportDetail detail : importEntity.getImportDetails()) {
            Product product = detail.getProduct();
            product.setQuantity(product.getQuantity() - detail.getQuantity());
            productRepository.save(product);
        }

        importRepository.delete(importEntity);
    }

    @Override
    public boolean existsById(Long id) {
        return importRepository.existsById(id);
    }
}