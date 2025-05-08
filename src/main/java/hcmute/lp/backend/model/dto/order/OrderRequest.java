package hcmute.lp.backend.model.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @Pattern(regexp = "^(PENDING|PROCESSING|COMPLETED|CANCELLED|DELIVERED|RETURNED)$",
            message = "Status must be one of: PENDING, PROCESSING, COMPLETED, CANCELLED, DELIVERED, RETURNED")
    private String status;

    private Long discountId;

    @NotEmpty(message = "Order must have at least one item")
    @Valid
    private List<OrderDetailRequest> orderDetails;
}