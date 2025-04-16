package hcmute.lp.backend.model.dto.brand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandRequest {
    private String name;
    private String description;
    private String logoUrl;
    private String origin;
    private String website;
    private String status;
}
