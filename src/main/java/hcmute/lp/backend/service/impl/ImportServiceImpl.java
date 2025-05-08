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


}