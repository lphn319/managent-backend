package hcmute.lp.backend.model.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private int quantity;
    private List<Integer> categoryIds;
    private Integer brandId;
}
