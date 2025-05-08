package hcmute.lp.backend.model.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    private int id;
    private String name;
    private String description;
    private int productCount;
    private Integer parentId;
    private List<CategoryDto> children;
}
