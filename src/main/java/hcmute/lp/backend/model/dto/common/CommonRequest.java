package hcmute.lp.backend.model.dto.common;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonRequest {
    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Value is required")
    private String value;
}