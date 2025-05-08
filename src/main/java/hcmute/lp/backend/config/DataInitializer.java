package hcmute.lp.backend.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hcmute.lp.backend.model.dto.brand.BrandRequest;
import hcmute.lp.backend.model.dto.category.CategoryRequest;
import hcmute.lp.backend.model.dto.department.DepartmentRequest;
import hcmute.lp.backend.model.dto.import_.ImportDetailRequest;
import hcmute.lp.backend.model.dto.import_.ImportRequest;
import hcmute.lp.backend.model.dto.product.ProductRequest;
import hcmute.lp.backend.model.dto.role.RoleRequest;
import hcmute.lp.backend.model.dto.supplier.SupplierRequest;
import hcmute.lp.backend.model.dto.user.UserRequest;
import hcmute.lp.backend.model.entity.*;
import hcmute.lp.backend.model.mapper.*;
import hcmute.lp.backend.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

    }
}