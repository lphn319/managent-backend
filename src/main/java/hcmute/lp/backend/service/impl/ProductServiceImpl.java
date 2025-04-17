package hcmute.lp.backend.service.impl;

import hcmute.lp.backend.exception.ResourceNotFoundException;
import hcmute.lp.backend.model.dto.product.ProductDto;
import hcmute.lp.backend.model.dto.product.ProductRequest;
import hcmute.lp.backend.model.entity.Brand;
import hcmute.lp.backend.model.entity.Category;
import hcmute.lp.backend.model.entity.Product;
import hcmute.lp.backend.model.mapper.ProductMapper;
import hcmute.lp.backend.repository.BrandRepository;
import hcmute.lp.backend.repository.CategoryRepository;
import hcmute.lp.backend.repository.ProductRepository;
import hcmute.lp.backend.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.toDtoList(products);
    }

    @Override
    public ProductDto getProductById(int id) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toDto(product);
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductRequest productRequest) {
        if (productRepository.existsByName(productRequest.getName())) {
            throw new IllegalArgumentException("Product already exists with name: " + productRequest.getName());
        }

        // First update basic fields
        Product product = productMapper.toEntity(productRequest);

        // Handle categories
        if (productRequest.getCategoryIds() != null && !productRequest.getCategoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(productRequest.getCategoryIds());
            product.setCategories(categories);
        }

        // Handle brand
        if (productRequest.getBrandId() != null ) {
            Brand brand = brandRepository.findById(productRequest.getBrandId())
                    .orElseThrow(() -> new EntityNotFoundException("Brand not found with id: " + productRequest.getBrandId()))   ;
            product.setBrand(brand);
        }

        // Save and return
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }


    @Override
    @Transactional
    public ProductDto updateProduct(int id, ProductRequest productRequest) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Product not found with id: " + id));

        // First update basic fields
        productMapper.updateEntityFromRequest(product, productRequest);

        // Handle categories if provided
        if (productRequest.getCategoryIds() != null) {
            List<Category> categories = categoryRepository.findAllById(productRequest.getCategoryIds());
            product.setCategories(categories);
        }

        // Handle brand if provided
        if (productRequest.getBrandId() != null) {
            Brand brand = brandRepository.findById(productRequest.getBrandId())
                    .orElseThrow(() -> new EntityNotFoundException("Brand not found with id: " + productRequest.getBrandId()));
            product.setBrand(brand);
        }

        Product updatedProduct = productRepository.save(product);
        return productMapper.toDto(updatedProduct);
    }

    @Override
    public void deleteProduct(int id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public boolean existsById(int id) {
        return productRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }
}
