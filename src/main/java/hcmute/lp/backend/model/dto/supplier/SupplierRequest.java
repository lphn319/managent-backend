package hcmute.lp.backend.model.dto.supplier;

import hcmute.lp.backend.model.entity.Supplier.SupplierStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierRequest {
    private String companyName;
    private String address;
    private String phone;
    private String email;
    private String description;
    private String logo;
    private SupplierStatus status = SupplierStatus.ACTIVE;
    private Set<Integer> categoryIds;
}