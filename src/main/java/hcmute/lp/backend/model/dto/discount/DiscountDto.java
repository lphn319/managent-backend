package hcmute.lp.backend.model.dto.discount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscountDto {
    private long id;
    private String code;
    private String name;
    private String description;
    private double discountRate;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;
}
