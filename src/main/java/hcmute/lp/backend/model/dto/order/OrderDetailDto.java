package hcmute.lp.backend.model.dto.order;
import hcmute.lp.backend.model.dto.product.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDto {
    private Long id;
    private ProductDto product;
    private int quantity;
    private double price;
    private double subtotal;
    private double discountAmount;
}