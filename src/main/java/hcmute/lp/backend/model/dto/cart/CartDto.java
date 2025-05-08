package hcmute.lp.backend.model.dto.cart;

import hcmute.lp.backend.model.dto.customer.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {
    private Long id;
    private CustomerDto customer;
    private double totalAmount;
    private List<CartItemDto> cartItems;
}