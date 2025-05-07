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
import java.util.Optional;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ImportRepository importRepository;
    @Autowired
    private ImportDetailRepository importDetailRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SupplierMapper supplierMapper;
    @Autowired
    private ImportMapper importMapper;
    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        try {
            // Khởi tạo theo thứ tự phụ thuộc
            initializeBrands();
            initializeCategories();
            initializeDepartments();
            initializeRoles();
            initializeUsers();
            initializeProducts();
            initializeSuppliers();
            initializeImports();

            // Log thông tin sau khi khởi tạo
            logEntityCounts();
        } catch (Exception e) {
            log.error("Error during data initialization", e);
            throw e;
        }
    }

    private void initializeCategories() {
        if (categoryRepository.count() == 0) {
            log.info("Initializing category data from JSON file...");
            try {
                Resource resource = new ClassPathResource("initial-categories.json");
                List<CategoryRequest> categoryRequests = loadFromJson(resource, new TypeReference<List<CategoryRequest>>() {});

                List<Category> categories = categoryRequests.stream()
                        .map(categoryMapper::toEntity)
                        .toList();

                List<Category> savedCategories = categoryRepository.saveAll(categories);
                log.info("Successfully initialized {} categories", savedCategories.size());

                // Log IDs để dễ dàng debug
                savedCategories.forEach(category ->
                        log.info("Saved category: id={}, name={}", category.getId(), category.getName()));
            } catch (IOException e) {
                log.error("Failed to initialize categories from JSON file", e);
            }
        }
    }

    private void initializeProducts() {
        if (productRepository.count() == 0) {
            log.info("Initializing product data from JSON file...");
            try {
                Resource resource = new ClassPathResource("initial-products.json");
                List<ProductRequest> productRequests = loadFromJson(resource, new TypeReference<List<ProductRequest>>() {});

                List<Product> products = new ArrayList<>();

                for (ProductRequest request : productRequests) {
                    try {
                        // Sử dụng mapper để chuyển đổi các trường cơ bản
                        Product product = productMapper.toEntity(request);

                        // Xử lý danh mục
                        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
                            List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
                            if (categories.size() != request.getCategoryIds().size()) {
                                log.warn("Some categories were not found for product: {}", request.getName());
                            }
                            product.setCategories(categories);
                        } else {
                            log.warn("No categories specified for product: {}", request.getName());
                            product.setCategories(new ArrayList<>());
                        }

                        // Xử lý thương hiệu
                        if (request.getBrandId() != null) {
                            Brand brand = brandRepository.findById(request.getBrandId())
                                    .orElseThrow(() -> new RuntimeException("Brand not found with id: " + request.getBrandId()));
                            product.setBrand(brand);
                        } else {
                            throw new RuntimeException("Brand ID is required for product: " + request.getName());
                        }

                        products.add(product);
                    } catch (Exception e) {
                        log.error("Error converting product request: {}", request.getName(), e);
                    }
                }

                List<Product> savedProducts = productRepository.saveAll(products);
                log.info("Successfully initialized {} products", savedProducts.size());
            } catch (IOException e) {
                log.error("Failed to initialize products from JSON file", e);
            }
        }
    }

    private void initializeBrands() {
        if (brandRepository.count() == 0) {
            log.info("Initializing brand data from JSON file...");
            try {
                Resource resource = new ClassPathResource("initial-brands.json");
                List<BrandRequest> brandRequests = loadFromJson(resource, new TypeReference<List<BrandRequest>>() {});

                List<Brand> brands = brandRequests.stream()
                        .map(brandMapper::toEntity)
                        .toList();

                List<Brand> savedBrands = brandRepository.saveAll(brands);
                log.info("Successfully initialized {} brands", savedBrands.size());

                // Log IDs để dễ dàng debug
                savedBrands.forEach(brand ->
                        log.info("Saved brand: id={}, name={}", brand.getId(), brand.getName()));
            } catch (IOException e) {
                log.error("Failed to initialize brands from JSON file", e);
            }
        }
    }

    private void initializeDepartments() {
        if (departmentRepository.count() == 0) {
            log.info("Initializing department data from JSON file...");
            try {
                Resource resource = new ClassPathResource("initial-departments.json");
                List<DepartmentRequest> departmentRequests = loadFromJson(resource, new TypeReference<List<DepartmentRequest>>() {});

                List<Department> departments = departmentRequests.stream()
                        .map(departmentMapper::toEntity)
                        .toList();

                List<Department> savedDepartments = departmentRepository.saveAll(departments);
                entityManager.flush();
                entityManager.clear();
                log.info("Successfully initialized {} departments", savedDepartments.size());

                // Log IDs để dễ dàng debug
                savedDepartments.forEach(department ->
                        log.info("Saved department: id={}, name={}", department.getId(), department.getName()));
            } catch (IOException e) {
                log.error("Failed to initialize departments from JSON file", e);
            }
        }
    }

    private void initializeRoles() {
        if (roleRepository.count() == 0) {
            log.info("Initializing role data from JSON file...");
            try {
                Resource resource = new ClassPathResource("initial-roles.json");
                List<RoleRequest> roleRequests = loadFromJson(resource, new TypeReference<List<RoleRequest>>() {});

                // Sử dụng RoleMapper để chuyển đổi
                List<Role> roles = roleRequests.stream()
                        .map(roleMapper::toEntity)
                        .toList();

                // Lưu roles
                List<Role> savedRoles = roleRepository.saveAll(roles);
                entityManager.flush();
                entityManager.clear();

                // Log để debug
                log.info("Saved {} roles", savedRoles.size());
                savedRoles.forEach(role ->
                        log.info("Saved role: id={}, name={}", role.getId(), role.getName()));

                // Verify count
                long count = roleRepository.count();
                log.info("Role count after save: {}", count);

            } catch (IOException e) {
                log.error("Failed to initialize roles from JSON file", e);
            }
        }
    }

    private void initializeUsers() {
        if (userRepository.count() == 0) {
            log.info("Initializing user data from JSON file...");
            try {
                Resource resource = new ClassPathResource("initial-users.json");
                List<UserRequest> userRequests = loadFromJson(resource, new TypeReference<List<UserRequest>>() {});

                List<User> users = new ArrayList<>();
                for (UserRequest request : userRequests) {
                    try {
//                        // Tìm role
//                        String roleName = getRoleNameFromId(request.getRoleId());
//                        Role role = roleRepository.findByName(roleName)
//                                .orElseThrow(() -> new RuntimeException("Role not found with name: " + roleName));
//
//                        // Tìm department nếu có
//                        Department department = null;
//                        if (request.getDepartmentId() != null) {
//                            String departmentName = getDepartmentNameFromId(request.getDepartmentId());
//                            department = departmentRepository.findByName(departmentName)
//                                    .orElse(null);
//                        }
                        Role role = roleRepository.findById(request.getRoleId())
                                .orElseThrow(() -> new RuntimeException("Role not found with id: " + request.getRoleId()));

                        Department department = null;
                        if (request.getDepartmentId() != null) {
                            department = departmentRepository.findById(request.getDepartmentId())
                                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + request.getDepartmentId()));
                        }

                        // Mã hóa mật khẩu
                        request.setPassword(passwordEncoder.encode(request.getPassword()));

                        // Sử dụng mapper
                        User user = userMapper.toEntity(request, role, department);
                        users.add(user);
                    } catch (Exception e) {
                        log.error("Error converting user request: {}", request.getName(), e);
                    }
                }

                List<User> savedUsers = userRepository.saveAll(users);
                log.info("Successfully initialized {} users", savedUsers.size());
            } catch (IOException e) {
                log.error("Failed to initialize users from JSON file", e);
            }
        }
    }

    private void initializeSuppliers() {
        if (supplierRepository.count() == 0) {
            log.info("Initializing supplier data from JSON file...");
            try {
                Resource resource = new ClassPathResource("initial-suppliers.json");
                List<SupplierRequest> supplierRequests = loadFromJson(resource, new TypeReference<List<SupplierRequest>>() {});

                List<Supplier> suppliers = supplierRequests.stream()
                        .map(supplierMapper::toEntity)
                        .toList();

                List<Supplier> savedSuppliers = supplierRepository.saveAll(suppliers);
                log.info("Successfully initialized {} suppliers", savedSuppliers.size());

                // Log IDs để dễ dàng debug
                savedSuppliers.forEach(supplier ->
                        log.info("Saved supplier: id={}, name={}", supplier.getId(), supplier.getCompanyName()));
            } catch (IOException e) {
                log.error("Failed to initialize suppliers from JSON file", e);
            }
        }
    }

    private void initializeImports() {
        if (importRepository.count() == 0) {
            log.info("Initializing import data from JSON file...");
            try {
                Resource resource = new ClassPathResource("initial-imports.json");
                List<ImportRequest> importRequests = loadFromJson(resource, new TypeReference<List<ImportRequest>>() {});

                List<Import> imports = new ArrayList<>();

                for (ImportRequest request : importRequests) {
                    try {
                        // Tạo import mới
                        Import importEntity = new Import();

                        // Thiết lập status
                        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
                            try {
                                importEntity.setStatus(Import.ImportStatus.fromValue(request.getStatus()));
                            } catch (IllegalArgumentException e) {
                                importEntity.setStatus(Import.ImportStatus.PROCESSING);
                            }
                        } else {
                            importEntity.setStatus(Import.ImportStatus.PROCESSING);
                        }

                        // Thiết lập supplier
                        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + request.getSupplierId()));
                        importEntity.setSupplier(supplier);

                        // Thiết lập employee
                        User employee = userRepository.findById((long)request.getEmployeeId())
                                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + request.getEmployeeId()));
                        importEntity.setEmployee(employee);

                        // Thiết lập notes
                        importEntity.setNotes(request.getNotes());

                        // Lưu import trước để có ID
                        Import savedImport = importRepository.save(importEntity);

                        // Xử lý import details
                        List<ImportDetail> importDetails = new ArrayList<>();
                        int totalQuantity = 0;
                        double totalAmount = 0;

                        for (ImportDetailRequest detailRequest : request.getImportDetails()) {
                            ImportDetail detail = new ImportDetail();
                            detail.setImportOrder(savedImport);

                            // Thiết lập product
                            Product product = productRepository.findById((int)detailRequest.getProductId())
                                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + detailRequest.getProductId()));
                            detail.setProduct(product);

                            // Thiết lập quantity và price
                            detail.setQuantity(detailRequest.getQuantity());
                            detail.setPrice(detailRequest.getPrice());

                            // Cập nhật tổng
                            totalQuantity += detailRequest.getQuantity();
                            totalAmount += detailRequest.getQuantity() * detailRequest.getPrice();

                            importDetails.add(detail);

                            // Cập nhật số lượng sản phẩm nếu status là COMPLETED
                            if (savedImport.getStatus() == Import.ImportStatus.COMPLETED) {
                                product.setQuantity(product.getQuantity() + detailRequest.getQuantity());
                                productRepository.save(product);
                            }
                        }

                        // Lưu tất cả chi tiết
                        importDetailRepository.saveAll(importDetails);

                        // Cập nhật import với tổng
                        savedImport.setQuantity(totalQuantity);
                        savedImport.setTotalAmount(totalAmount);
                        savedImport.setImportDetails(importDetails);

                        // Lưu import đã cập nhật
                        imports.add(importRepository.save(savedImport));

                        log.info("Created import: id={}, supplier={}, status={}",
                                savedImport.getId(), savedImport.getSupplier().getCompanyName(), savedImport.getStatus());

                    } catch (Exception e) {
                        log.error("Error creating import for supplier id {}: {}", request.getSupplierId(), e.getMessage());
                    }
                }

                log.info("Successfully initialized {} imports", imports.size());
            } catch (IOException e) {
                log.error("Failed to initialize imports from JSON file", e);
            }
        }
    }

    private <T> T loadFromJson(Resource resource, TypeReference<T> typeReference) throws IOException {
        InputStream inputStream = resource.getInputStream();
        return objectMapper.readValue(inputStream, typeReference);
    }

    private void logEntityCounts() {
        log.info("Data initialization complete. Current counts:");
        log.info("Brands: {}", brandRepository.count());
        log.info("Categories: {}", categoryRepository.count());
        log.info("Departments: {}", departmentRepository.count());
        log.info("Roles: {}", roleRepository.count());
        log.info("Users: {}", userRepository.count());
        log.info("Products: {}", productRepository.count());
        log.info("Suppliers: {}", supplierRepository.count());
        log.info("Imports: {}", importRepository.count());
    }
}