package hcmute.lp.backend.model.dto.supplier;

import hcmute.lp.backend.model.dto.category.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

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
    private String logo;
    private String status; // Sửa lại từ Enum sang String
    private Set<CategoryDto> categories;
}