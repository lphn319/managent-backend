package hcmute.lp.backend.service;

import hcmute.lp.backend.model.dto.import_.ImportDto;
import hcmute.lp.backend.model.dto.import_.ImportRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ImportService {
    List<ImportDto> getAllImports();
}