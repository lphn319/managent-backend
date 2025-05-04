package hcmute.lp.backend.model.dto.import_;

import hcmute.lp.backend.model.dto.product.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImportDetailDto {
    private Long id; // Thay đổi từ int sang Long
    private ProductDto product;
    private int quantity;
    private double price;
}