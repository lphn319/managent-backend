package hcmute.lp.backend.model.dto.supplier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierRequest {
    private String companyName;
    private String address;
    private String phone;
    private String email;
    private String description;
}
