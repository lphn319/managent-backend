package hcmute.lp.backend.config.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hcmute.lp.backend.model.dto.role.RoleRequest;
import hcmute.lp.backend.model.entity.Role;
import hcmute.lp.backend.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
public class RoleDataInitializer implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            log.info("Initializing role data from JSON file...");
            try {
                // Đọc file JSON
                 Resource resource = new ClassPathResource("initial-roles.json");
                 InputStream inputStream = resource.getInputStream();

                 // Parse JSON thành danh sách RoleRequest
                 List<RoleRequest> roleRequests = objectMapper.readValue(
                         inputStream,
                         new TypeReference<List<RoleRequest>>() {}
                 );

                 // Chuyển đổi RoleRequest thành Role entity
                 List<Role> roles = roleRequests.stream()
                         .map(this::convertToRole)
                         .toList();

                 // Lưu vào database
                 roleRepository.saveAll(roles);

            } catch (Exception e) {
                log.error("Failed to initialize roles from JSON file", e);
            }
        } else {
            log.info("Roles already exist in database, skipping initialization");
        }
    }

    private Role convertToRole(RoleRequest roleRequest) {
        Role role = new Role();
        role.setName(roleRequest.getName());
        return role;
    }
}
