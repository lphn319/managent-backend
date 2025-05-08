package hcmute.lp.backend.model.dto.import_;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportDetailRequest {
    private long productId;
    private int quantity;
    private double price;
}
