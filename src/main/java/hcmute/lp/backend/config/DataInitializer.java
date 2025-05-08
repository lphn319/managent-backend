package hcmute.lp.backend.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hcmute.lp.backend.model.dto.brand.BrandRequest;
import hcmute.lp.backend.model.dto.category.CategoryRequest;
import hcmute.lp.backend.model.dto.department.DepartmentRequest;
import hcmute.lp.backend.model.dto.discount.DiscountRequest;
import hcmute.lp.backend.model.dto.import_.ImportDetailRequest;
import hcmute.lp.backend.model.dto.import_.ImportRequest;
import hcmute.lp.backend.model.dto.order.OrderDetailRequest;
import hcmute.lp.backend.model.dto.order.OrderRequest;
import hcmute.lp.backend.model.dto.permission.PermissionRequest;
import hcmute.lp.backend.model.dto.product.ProductRequest;
import hcmute.lp.backend.model.dto.role.RoleRequest;
import hcmute.lp.backend.model.dto.supplier.SupplierRequest;
import hcmute.lp.backend.model.dto.user.UserRequest;
import hcmute.lp.backend.model.entity.*;
import hcmute.lp.backend.model.common.CommonCategories;
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
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ImportRepository importRepository;

    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Inject các Mapper
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private SupplierMapper supplierMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (isDataAlreadyInitialized()) {
            log.info("Data already initialized. Skipping initialization.");
            return;
        }

        log.info("Starting data initialization...");

        // Khởi tạo dữ liệu theo thứ tự phù hợp
        initializeCommon();
        initializePermissions();
        initializeRoles();
        initializeDepartments();
        initializeUsers();
        initializeCategories();
        initializeBrands();
        initializeProducts();
        initializeSuppliers();
        initializeDiscounts();
        initializeCarts();
        initializeOrders();
        initializeImports();
        log.info("Data initialization completed.");
    }

    private boolean isDataAlreadyInitialized() {
        // Kiểm tra xem dữ liệu đã được khởi tạo chưa
        return permissionRepository.count() > 0 &&
                roleRepository.count() > 0 &&
                userRepository.count() > 0;
    }

    private void initializeCommon() throws IOException {
        log.info("Initializing common data...");
        Resource resource = new ClassPathResource("data/common.json");
        List<Common> commons = loadFromJson(resource, new TypeReference<List<Common>>() {});
        commonRepository.saveAll(commons);
    }

    private void initializePermissions() throws IOException {
        log.info("Initializing permissions...");
        Resource resource = new ClassPathResource("data/permissions.json");
        List<PermissionRequest> permissionRequests = loadFromJson(resource, new TypeReference<List<PermissionRequest>>() {});

        permissionRequests.forEach(request -> {
            Permission permission = permissionMapper.toEntity(request);
            permissionRepository.save(permission);
        });
    }

    private void initializeRoles() throws IOException {
        log.info("Initializing roles...");
        Resource resource = new ClassPathResource("data/roles.json");
        List<RoleRequest> roleRequests = loadFromJson(resource, new TypeReference<List<RoleRequest>>() {});

        roleRequests.forEach(request -> {
            Role role = roleMapper.toEntity(request);
            roleRepository.save(role);
        });
    }

    private void initializeDepartments() throws IOException {
        log.info("Initializing departments...");
        Resource resource = new ClassPathResource("data/departments.json");
        List<DepartmentRequest> departmentRequests = loadFromJson(resource, new TypeReference<List<DepartmentRequest>>() {});

        departmentRequests.forEach(request -> {
            Department department = departmentMapper.toEntity(request);
            departmentRepository.save(department);
        });
    }

    private void initializeUsers() throws IOException {
        log.info("Initializing users...");
        Resource resource = new ClassPathResource("data/users.json");
        List<UserRequest> userRequests = loadFromJson(resource, new TypeReference<List<UserRequest>>() {});

        userRequests.forEach(request -> {
            // Lấy role và department
            Role role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found with id: " + request.getRoleId()));

            Department department = null;
            if (request.getDepartmentId() != null) {
                department = departmentRepository.findById(request.getDepartmentId())
                        .orElse(null);
            }

            // Mã hóa mật khẩu
            String encodedPassword = passwordEncoder.encode(request.getPassword());
            request.setPassword(encodedPassword);

            // Tạo và lưu user
            User user = userMapper.toEntity(request, role, department);
            user = userRepository.save(user);

            // Tạo Customer hoặc Employee dựa vào role
            if (role.isCustomer()) {
                Customer customer = new Customer();
                customer.setUser(user);
                customer.setFullName(request.getName());
                customer.setPhoneNumber(request.getPhoneNumber());
                customer.setDateOfBirth(request.getDateOfBirth());
                customer.setGender(request.getGender());
                customer.setRegistrationDate(LocalDate.now());
                customer.setMembershipLevel(CommonCategories.MembershipLevel.BRONZE);
                customer.setLoyaltyPoints(0);
                entityManager.persist(customer);
            } else if (role.isEmployee() || role.isManager() || role.isAdmin()) {
                Employee employee = new Employee();
                employee.setUser(user);
                employee.setFullName(request.getName());
                employee.setPhoneNumber(request.getPhoneNumber());
                employee.setDateOfBirth(request.getDateOfBirth());
                employee.setGender(request.getGender());
                employee.setDepartment(department);
                employee.setEmployeeId("EMP" + String.format("%05d", user.getId()));
                entityManager.persist(employee);
            }
        });
    }

    private void initializeCategories() throws IOException {
        log.info("Initializing categories...");
        Resource resource = new ClassPathResource("data/categories.json");
        List<CategoryRequest> categoryRequests = loadFromJson(resource, new TypeReference<List<CategoryRequest>>() {});

        // Trước tiên, lưu tất cả các danh mục gốc (không có parent)
        categoryRequests.stream()
                .filter(request -> request.getParentId() == null)
                .forEach(request -> {
                    Category category = categoryMapper.toEntity(request);
                    categoryRepository.save(category);
                });

        // Sau đó, lưu các danh mục con
        categoryRequests.stream()
                .filter(request -> request.getParentId() != null)
                .forEach(request -> {
                    Category category = categoryMapper.toEntity(request);
                    categoryRepository.save(category);
                });
    }

    private void initializeBrands() throws IOException {
        log.info("Initializing brands...");
        Resource resource = new ClassPathResource("data/brands.json");
        List<BrandRequest> brandRequests = loadFromJson(resource, new TypeReference<List<BrandRequest>>() {});

        brandRequests.forEach(request -> {
            Brand brand = brandMapper.toEntity(request);
            brandRepository.save(brand);
        });
    }

    private void initializeProducts() throws IOException {
        log.info("Initializing products...");
        Resource resource = new ClassPathResource("data/products.json");
        List<ProductRequest> productRequests = loadFromJson(resource, new TypeReference<List<ProductRequest>>() {});

        productRequests.forEach(request -> {
            // Tạo product từ request
            Product product = productMapper.toEntity(request);

            // Thiết lập brand
            if (request.getBrandId() != null) {
                Brand brand = brandRepository.findById(request.getBrandId())
                        .orElseThrow(() -> new RuntimeException("Brand not found with id: " + request.getBrandId()));
                product.setBrand(brand);
            }

            // Thiết lập categories
            if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
                List<Category> categories = new ArrayList<>();
                request.getCategoryIds().forEach(categoryId -> {
                    categoryRepository.findById(categoryId)
                            .ifPresent(categories::add);
                });
                product.setCategories(categories);
            }

            productRepository.save(product);
        });
    }

    private void initializeSuppliers() throws IOException {
        log.info("Initializing suppliers...");
        Resource resource = new ClassPathResource("data/suppliers.json");
        List<SupplierRequest> supplierRequests = loadFromJson(resource, new TypeReference<List<SupplierRequest>>() {});

        supplierRequests.forEach(request -> {
            Supplier supplier = supplierMapper.toEntity(request);

            // Thiết lập categories cho supplier
            if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
                Set<Category> categories = new HashSet<>();
                request.getCategoryIds().forEach(categoryId -> {
                    categoryRepository.findById(categoryId)
                            .ifPresent(categories::add);
                });
                supplier.setCategories(categories);
            }

            supplierRepository.save(supplier);
        });
    }

    private void initializeDiscounts() throws IOException {
        log.info("Initializing discounts...");
        Resource resource = new ClassPathResource("data/discounts.json");
        List<DiscountRequest> discountRequests = loadFromJson(resource, new TypeReference<List<DiscountRequest>>() {});

        discountRequests.forEach(request -> {
            Discount discount = DiscountMapper.DISCOUNT_MAPPER.toEntity(request);
            discountRepository.save(discount);
        });
    }
    private void initializeCarts() throws IOException {
        log.info("Initializing carts...");
        Resource resource = new ClassPathResource("data/carts.json");
        List<Map<String, Object>> cartDataList = loadFromJson(resource, new TypeReference<List<Map<String, Object>>>() {});

        for (Map<String, Object> cartData : cartDataList) {
            // Lấy customer
            Long customerId = Long.valueOf(cartData.get("customerId").toString());
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

            // Tạo cart
            Cart cart = new Cart();
            cart.setCustomer(customer);
            cart.setTotalAmount(0); // Sẽ được cập nhật sau

            cart = cartRepository.save(cart);

            // Xử lý cart items
            List<Map<String, Object>> cartItemsData = (List<Map<String, Object>>) cartData.get("cartItems");
            List<CartItem> cartItems = new ArrayList<>();

            for (Map<String, Object> cartItemData : cartItemsData) {
                Long productId = Long.valueOf(cartItemData.get("productId").toString());
                Integer quantity = Integer.valueOf(cartItemData.get("quantity").toString());
                Boolean isSelected = (Boolean) cartItemData.get("isSelected");

                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

                CartItem cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                cartItem.setSubtotal(product.getPrice() * quantity);
                cartItem.setSelected(isSelected != null ? isSelected : false);

                cartItems.add(cartItem);
                entityManager.persist(cartItem);
            }

            // Cập nhật tổng tiền cart
            double totalAmount = cartItems.stream()
                    .filter(CartItem::isSelected)
                    .mapToDouble(CartItem::getSubtotal)
                    .sum();
            cart.setTotalAmount(totalAmount);
            cart.setCartItems(cartItems);

            cartRepository.save(cart);
        }
    }

    private void initializeOrders() throws IOException {
        log.info("Initializing orders...");
        Resource resource = new ClassPathResource("data/orders.json");
        List<OrderRequest> orderRequests = loadFromJson(resource, new TypeReference<List<OrderRequest>>() {});

        for (OrderRequest orderRequest : orderRequests) {
            // Lấy customer
            Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + orderRequest.getCustomerId()));

            // Tạo order
            Order order = new Order();
            order.setCustomer(customer);
            order.setStatus(orderRequest.getStatus());

            // Xử lý discount nếu có
            if (orderRequest.getDiscountId() != null) {
                Discount discount = discountRepository.findById(orderRequest.getDiscountId())
                        .orElse(null);
                order.setDiscount(discount);
            }

            order = orderRepository.save(order);

            // Xử lý order details
            List<OrderDetail> orderDetails = new ArrayList<>();

            for (OrderDetailRequest detailRequest : orderRequest.getOrderDetails()) {
                Product product = productRepository.findById(detailRequest.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found with id: " + detailRequest.getProductId()));

                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProduct(product);
                orderDetail.setQuantity(detailRequest.getQuantity());
                orderDetail.setPrice(product.getPrice());

                // Xử lý giảm giá chi tiết nếu có
                double discountAmount = detailRequest.getDiscountAmount() != null ? detailRequest.getDiscountAmount() : 0;
                orderDetail.setDiscountAmount(discountAmount);

                // Tính tổng tiền
                orderDetail.setSubtotal(orderDetail.getPrice() * orderDetail.getQuantity() - discountAmount);

                orderDetails.add(orderDetail);
                entityManager.persist(orderDetail);
            }

            order.setOrderDetails(orderDetails);

            // Cập nhật tổng tiền đơn hàng
            order.updateTotalAmount();
            orderRepository.save(order);
        }
    }

    private void initializeImports() throws IOException {
        log.info("Initializing imports...");
        Resource resource = new ClassPathResource("data/imports.json");
        List<ImportRequest> importRequests = loadFromJson(resource, new TypeReference<List<ImportRequest>>() {});

        for (ImportRequest importRequest : importRequests) {
            // Lấy supplier
            Supplier supplier = supplierRepository.findById(importRequest.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + importRequest.getSupplierId()));

            // Tạo import
            Import importEntity = new Import();
            importEntity.setSupplier(supplier);
            importEntity.setStatus(importRequest.getStatus());
            importEntity.setNotes(importRequest.getNotes());
            importEntity.setTotalAmount(0); // Sẽ được cập nhật sau
            importEntity.setQuantity(0); // Sẽ được cập nhật sau

            importEntity = importRepository.save(importEntity);

            // Xử lý import details
            List<ImportDetail> importDetails = new ArrayList<>();
            int totalQuantity = 0;
            double totalAmount = 0;

            for (ImportDetailRequest detailRequest : importRequest.getImportDetails()) {
                Product product = productRepository.findById(detailRequest.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found with id: " + detailRequest.getProductId()));

                ImportDetail importDetail = new ImportDetail();
                importDetail.setImportOrder(importEntity);
                importDetail.setProduct(product);
                importDetail.setQuantity(detailRequest.getQuantity());
                importDetail.setPrice(detailRequest.getPrice());

                importDetails.add(importDetail);
                entityManager.persist(importDetail);

                // Cập nhật số lượng và tổng tiền
                totalQuantity += detailRequest.getQuantity();
                totalAmount += detailRequest.getPrice() * detailRequest.getQuantity();

                // Cập nhật số lượng sản phẩm
                product.setQuantity(product.getQuantity() + detailRequest.getQuantity());
                productRepository.save(product);
            }

            // Cập nhật tổng số lượng và tổng tiền
            importEntity.setQuantity(totalQuantity);
            importEntity.setTotalAmount(totalAmount);
            importEntity.setImportDetails(importDetails);

            importRepository.save(importEntity);
        }
    }

    private <T> List<T> loadFromJson(Resource resource, TypeReference<List<T>> typeReference) throws IOException {
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            log.error("Failed to read JSON file: {}", resource.getFilename(), e);
            throw e;
        }
    }
}