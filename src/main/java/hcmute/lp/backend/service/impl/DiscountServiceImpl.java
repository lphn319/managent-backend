package hcmute.lp.backend.service.impl;

import hcmute.lp.backend.exception.ResourceNotFoundException;
import hcmute.lp.backend.model.dto.discount.DiscountDto;
import hcmute.lp.backend.model.dto.discount.DiscountRequest;
import hcmute.lp.backend.model.entity.Discount;
import hcmute.lp.backend.model.mapper.DiscountMapper;
import hcmute.lp.backend.repository.DiscountRepository;
import hcmute.lp.backend.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService {
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private DiscountMapper discountMapper;

    @Override
    public List<DiscountDto> getAllDiscounts() {
        List<Discount> discounts = discountRepository.findAll();
        return discountMapper.toDtoList(discounts);
    }

    @Override
    public DiscountDto getDiscountById(long id) {
        Discount discount = discountRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Discount not found with id: " + id));
        return discountMapper.toDto(discount);
    }

    @Override
    public DiscountDto getDiscountByCode(String code) {
        Discount discount = discountRepository.findByCode(code);
        if (discount == null) {
            throw new ResourceNotFoundException("Discount not found with code: " + code);
        }
        return discountMapper.toDto(discount);
    }

    @Override
    @Transactional
    public DiscountDto createDiscount(DiscountRequest discountRequest) {
        if (discountRepository.findByCode(discountRequest.getCode()) != null) {
            throw new IllegalArgumentException("Discount already exists with code: " + discountRequest.getCode());
        }
        if (discountRepository.existsByName(discountRequest.getName())) {
            throw new IllegalArgumentException("Discount already exists with name: " + discountRequest.getName());
        }
        // First update basic fields
        Discount discount = discountMapper.toEntity(discountRequest);
        Discount savedDiscount = discountRepository.save(discount);
        return discountMapper.toDto(savedDiscount);
    }

    @Override
    @Transactional
    public DiscountDto updateDiscount(long id, DiscountRequest discountRequest) {
       Discount discount = discountRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Discount not found with id: " + id));

       discountMapper.updateEntityFromRequest(discount, discountRequest);
       Discount updatedDiscount = discountRepository.save(discount);
       return discountMapper.toDto(updatedDiscount);
    }

    @Override
    public void deleteDiscount(long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found with id: " + id));
        discountRepository.delete(discount);
    }

    @Override
    public boolean existsById(long id) {
        return discountRepository.existsById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return discountRepository.existsByCode(code);
    }

    @Override
    public boolean existsByName(String name) {
        return discountRepository.existsByName(name);
    }

    @Override
    public boolean isDiscountActive(long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found with id: " + id));
        return discount.isActive();
    }

    @Override
    public Page<DiscountDto> getDiscountsPaginated(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Page<Discount> discountPage = discountRepository.findAll(PageRequest.of(page, size, Sort.by(direction, sortBy)));
        return discountPage.map(discountMapper::toDto);
    }
}
