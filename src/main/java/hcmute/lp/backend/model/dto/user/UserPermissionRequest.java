package hcmute.lp.backend.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPermissionRequest {
    private Long userId;
    private Set<Integer> permissionIds;
}