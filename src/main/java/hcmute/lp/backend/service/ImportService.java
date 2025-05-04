package hcmute.lp.backend.service;

import hcmute.lp.backend.model.dto.import_.ImportDto;
import hcmute.lp.backend.model.dto.import_.ImportRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ImportService {
    List<ImportDto> getAllImports();

    Page<ImportDto> getImportsPaginated(
            int page, int size, String sortBy, String sortDirection,
            Long supplierId, Long employeeId, String status); // Thay đổi các tham số

    ImportDto getImportById(Long id); // Thay đổi từ int sang Long

    ImportDto createImport(ImportRequest importRequest);

    ImportDto updateImport(Long id, ImportRequest importRequest); // Thay đổi từ int sang Long

    ImportDto updateStatus(Long id, String status); // Thay đổi từ int sang Long

    void deleteImport(Long id); // Thay đổi từ int sang Long

    boolean existsById(Long id); // Thay đổi từ int sang Long
}