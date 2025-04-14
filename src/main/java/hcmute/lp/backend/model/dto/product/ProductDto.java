package hcmute.lp.backend.model.dto.product;

import hcmute.lp.backend.model.dto.brand.BrandDto;
import hcmute.lp.backend.model.dto.category.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private int id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private int quantity;
    private List<CategoryDto> categories;
    private BrandDto brand;
}
