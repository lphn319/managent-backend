package hcmute.lp.backend.model.dto.order;

import hcmute.lp.backend.model.dto.customer.CustomerDto;
import hcmute.lp.backend.model.dto.discount.DiscountDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private UUID id;
    private CustomerDto customer;
    private String status;
    private double totalAmount;
    private DiscountDto discount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderDetailDto> orderDetails;
}