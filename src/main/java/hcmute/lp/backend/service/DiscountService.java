package hcmute.lp.backend.service;

import hcmute.lp.backend.model.dto.discount.DiscountDto;
import hcmute.lp.backend.model.dto.discount.DiscountRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DiscountService {
    List<DiscountDto> getAllDiscounts();
    DiscountDto getDiscountById(long id);
    DiscountDto getDiscountByCode(String code);
    DiscountDto createDiscount(DiscountRequest discountRequest);
    DiscountDto updateDiscount(long id, DiscountRequest discountRequest);
    void deleteDiscount(long id);
    boolean existsById(long id);
    boolean existsByCode(String code);
    boolean existsByName(String name);
    boolean isDiscountActive(long id);

    Page<DiscountDto> getDiscountsPaginated(int page, int size, String sortBy, String sortDirection);
}
