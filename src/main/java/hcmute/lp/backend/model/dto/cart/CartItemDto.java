package hcmute.lp.backend.model.dto.cart;

import hcmute.lp.backend.model.dto.product.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {
    private Long id;
    private ProductDto product;
    private int quantity;
    private double subtotal;
    private boolean isSelected;
}