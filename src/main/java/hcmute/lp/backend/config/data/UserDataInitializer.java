package hcmute.lp.backend.config.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hcmute.lp.backend.model.dto.user.UserRequest;
import hcmute.lp.backend.model.entity.User;
import hcmute.lp.backend.repository.RoleRepository;
import hcmute.lp.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Role;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
public class UserDataInitializer implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("Initializing user data from JSON file...");
            try {
                // Đọc file JSON
                Resource resource = new ClassPathResource("initial-users.json");
                 InputStream inputStream = resource.getInputStream();

                 // Parse JSON thành danh sách UserRequest
                 List<UserRequest> userRequests = objectMapper.readValue(
                         inputStream,
                         new TypeReference<List<UserRequest>>() {}
                 );

                 // Chuyển đổi UserRequest thành User entity
                 List<User> users = userRequests.stream()
                         .map(this::convertToUser)
                         .toList();

                 // Lưu vào database
                 userRepository.saveAll(users);

                log.info("Successfully initialized {} users", 0);
            } catch (Exception e) {
                log.error("Failed to initialize users from JSON file", e);
            }
        } else {
            log.info("Users already exist in database, skipping initialization");
        }
    }

    private User convertToUser(UserRequest userRequest) {
        Role role = (Role) roleRepository.findById(userRequest.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + userRequest.getRoleId()));
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setPassword(userRequest.getPassword());
        user.setRole((hcmute.lp.backend.model.entity.Role) role);
        return user;
    }
}
