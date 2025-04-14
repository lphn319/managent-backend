package hcmute.lp.backend.model.dto.department;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentDto {
    private int id;
    private String name;
    private String description;
}
