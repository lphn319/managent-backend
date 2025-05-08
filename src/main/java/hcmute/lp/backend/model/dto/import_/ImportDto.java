package hcmute.lp.backend.model.dto.import_;

import hcmute.lp.backend.model.dto.supplier.SupplierDto;
import hcmute.lp.backend.model.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImportDto {
    private Long id;
    private String status;
    private double totalAmount;
    private int quantity;
    private String notes;
    private SupplierDto supplier;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ImportDetailDto> importDetails;
}