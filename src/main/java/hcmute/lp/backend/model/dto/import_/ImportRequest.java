package hcmute.lp.backend.model.dto.import_;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportRequest {
    private int supplierId;
    private String status;
    private String notes;
    private List<ImportDetailRequest> importDetails;
}