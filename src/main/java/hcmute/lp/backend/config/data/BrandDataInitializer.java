package hcmute.lp.backend.config.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hcmute.lp.backend.model.dto.brand.BrandRequest;
import hcmute.lp.backend.model.entity.Brand;
import hcmute.lp.backend.repository.BrandRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
public class BrandDataInitializer implements CommandLineRunner {
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (brandRepository.count() == 0) {
            log.info("Initializing brand data from JSON file...");
            try {
                // Đọc file JSON
                Resource resource = new ClassPathResource("initial-brands.json");
                InputStream inputStream = resource.getInputStream();

                // Parse JSON thành danh sách BrandRequest
                List<BrandRequest> brandRequests = objectMapper.readValue(
                        inputStream,
                        new TypeReference<List<BrandRequest>>() {}
                );

                // Chuyển đổi BrandRequest thành Brand entity
                List<Brand> brands = brandRequests.stream()
                        .map(this::convertToBrand)
                        .toList();

                // Lưu vào database
                brandRepository.saveAll(brands);

                log.info("Successfully initialized {} brands", brands.size());
            } catch (IOException e) {
                log.error("Failed to initialize brands from JSON file", e);
            }
        } else {
            log.info("Brands already exist in database, skipping initialization");
                }
    }

    private Brand convertToBrand(BrandRequest request) {
        Brand brand = new Brand();
        brand.setName(request.getName());
        brand.setDescription(request.getDescription());
        brand.setLogoUrl(request.getLogoUrl());
        brand.setOrigin(request.getOrigin());
        brand.setWebsite(request.getWebsite());

        try {
            brand.setStatus(Brand.BrandStatus.valueOf(request.getStatus()));
        } catch (IllegalArgumentException e) {
            log.warn("Invalid status for brand {}, defaulting to ACTIVE", request.getName());
            brand.setStatus(Brand.BrandStatus.ACTIVE);
        }

        return brand;
    }
}
