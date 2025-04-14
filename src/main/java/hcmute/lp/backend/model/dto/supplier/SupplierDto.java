package hcmute.lp.backend.model.dto.supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierDto {
    private int id;
    private String companyName;
    private String address;
    private String phone;
    private String email;
    private String description;
}
