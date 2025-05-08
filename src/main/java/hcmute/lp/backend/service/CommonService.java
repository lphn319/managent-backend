package hcmute.lp.backend.service;

import hcmute.lp.backend.model.entity.Common;
import hcmute.lp.backend.repository.CommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommonService {

    @Autowired
    private CommonRepository commonRepository;

    @Cacheable("commons")
    public List<Common> getByCategory(String category) {
        return commonRepository.findByCategory(category);
    }

    public Optional<Common> getByCategoryAndValue(String category, String value) {
        return commonRepository.findByCategoryAndValue(category, value);
    }
}
