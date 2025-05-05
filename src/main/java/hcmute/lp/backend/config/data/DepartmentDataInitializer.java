package hcmute.lp.backend.config.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hcmute.lp.backend.model.dto.department.DepartmentRequest;
import hcmute.lp.backend.model.entity.Department;
import hcmute.lp.backend.repository.DepartmentRepository;
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
public class DepartmentDataInitializer implements CommandLineRunner {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (departmentRepository.count() == 0) {
            log.info("Initializing department data from JSON file...");
            try {
                // Đọc file JSON
                Resource resource = new ClassPathResource("initial-departments.json");
                InputStream inputStream = resource.getInputStream();

                // Parse JSON thành danh sách DepartmentRequest
                List<DepartmentRequest> departmentRequests = objectMapper.readValue(
                        inputStream,
                        new TypeReference<List<DepartmentRequest>>() {}
                );

                // Chuyển đổi DepartmentRequest thành Department entity
                List<Department> departments = departmentRequests.stream()
                        .map(this::convertToDepartment)
                        .toList();

                // Lưu vào database
                departmentRepository.saveAll(departments);

                log.info("Successfully initialized {} departments", departments.size());
            } catch (Exception e) {
                log.error("Failed to initialize departments from JSON file", e);
            }
        } else {
            log.info("Departments already exist in database, skipping initialization");
        }
    }

    private Department convertToDepartment(DepartmentRequest departmentRequest) {
        Department department = new Department();
        department.setName(departmentRequest.getName());
        department.setDescription(departmentRequest.getDescription());
        return department;
    }
}
