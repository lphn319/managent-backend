package hcmute.lp.backend.service.impl;

import hcmute.lp.backend.exception.ResourceNotFoundException;
import hcmute.lp.backend.model.dto.brand.BrandDto;
import hcmute.lp.backend.model.dto.brand.BrandRequest;
import hcmute.lp.backend.model.entity.Brand;
import hcmute.lp.backend.model.mapper.BrandMapper;
import hcmute.lp.backend.repository.BrandRepository;
import hcmute.lp.backend.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private BrandMapper brandMapper;

    @Override
    public List<BrandDto> getAllBrands() {
        List<Brand> brands = brandRepository.findAll();
        return brandMapper.toDtoList(brands);
    }

    @Override
    public BrandDto getBrandById(int id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Brand not found with id: " + id));
        return brandMapper.toDto(brand);
    }

    @Override
    @Transactional
    public BrandDto createBrand(BrandRequest brandRequest) {
        if (brandRepository.existsByName(brandRequest.getName())) {
            throw new IllegalArgumentException("Brand already exists with name: " + brandRequest.getName());
        }
        Brand brand = brandMapper.toEntity(brandRequest);
        Brand savedBrand = brandRepository.save(brand);
        return brandMapper.toDto(savedBrand);
    }

    @Override
    @Transactional
    public List<BrandDto> createBrands(List<BrandRequest> brandRequests) {
        // Kiểm tra tên trùng lặp trong danh sách gửi lên
        Set<String> requestNames = new HashSet<>();
        for (BrandRequest request : brandRequests) {
            if (!requestNames.add(request.getName())) {
                throw new IllegalArgumentException("Danh sách chứa tên thương hiệu trùng lặp: " + request.getName());
            }
        }

        // Kiểm tra tên đã tồn tại trong database
        for (BrandRequest request : brandRequests) {
            if (brandRepository.existsByName(request.getName())) {
                throw new IllegalArgumentException("Thương hiệu với tên đã tồn tại: " + request.getName());
            }
        }

        List<Brand> brands = brandRequests.stream()
                .map(brandMapper::toEntity)
                .collect(Collectors.toList());

        List<Brand> savedBrands = brandRepository.saveAll(brands);
        return brandMapper.toDtoList(savedBrands);
    }

    @Override
    @Transactional
    public BrandDto updateBrand(int id, BrandRequest brandRequest) {
        Brand brand = brandRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Brand not found with id: " + id));

        if (!brand.getName().equals(brandRequest.getName()) && brandRepository.existsByName(brandRequest.getName())) {
            throw new IllegalArgumentException("Brand with this name already exists: " + brandRequest.getName());
        }

        brandMapper.updateEntityFromRequest(brand, brandRequest);
        Brand updatedBrand = brandRepository.save(brand);
        return brandMapper.toDto(updatedBrand);
    }

    @Override
    @Transactional
    public void deleteBrand(int id) {
        if (!brandRepository.existsById(id)) {
            throw new ResourceNotFoundException("Brand not found with id: " + id);
        }
        brandRepository.deleteById(id);
    }

    @Override
    public boolean existsById(int id) {
        return brandRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return brandRepository.existsByName(name);
    }

    @Override
    public BrandDto updateBrandStatus(int id, String status) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));

        brand.setStatus(Brand.BrandStatus.valueOf(status));
        brand = brandRepository.save(brand);
        return brandMapper.toDto(brand);
    }

    @Override
    public List<BrandDto> getFeaturedBrands(int limit) {
        List<Brand> activeAndOrderedBrands = brandRepository.findByStatusOrderByProductsDesc(Brand.BrandStatus.ACTIVE);
        return brandMapper.toDtoList(activeAndOrderedBrands.stream()
                .limit(limit)
                .collect(Collectors.toList()));
    }

    @Override
    public Map<String, Integer> getBrandStatistics() {
        Map<String, Integer> statistics = new HashMap<>();

        List<Brand> allBrands = brandRepository.findAll();
        List<Brand> activeBrands = brandRepository.findByStatus(Brand.BrandStatus.ACTIVE);

        statistics.put("totalBrands", allBrands.size());
        statistics.put("activeBrands", activeBrands.size());
        statistics.put("inactiveBrands", allBrands.size() - activeBrands.size());

        return statistics;
    }

    @Override
    public Page<BrandDto> getBrandsPaginated(int page, int size, String sortBy, String sortDirection, String status) {
        // Tạo đối tượng Pageable với thông số phân trang
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Brand> brandPage;

        // Nếu có tham số status, lọc theo status
        if (status != null && !status.isEmpty()) {
            try {
                Brand.BrandStatus brandStatus = Brand.BrandStatus.valueOf(status);
                brandPage = brandRepository.findByStatus(brandStatus, pageable);
            } catch (IllegalArgumentException e) {
                // Nếu status không hợp lệ, trả về tất cả
                brandPage = brandRepository.findAll(pageable);
            }
        } else {
            // Nếu không có tham số status, trả về tất cả
            brandPage = brandRepository.findAll(pageable);
        }

        // Chuyển đổi Page<Brand> thành Page<BrandDto>
        return brandPage.map(brandMapper::toDto);
    }
}