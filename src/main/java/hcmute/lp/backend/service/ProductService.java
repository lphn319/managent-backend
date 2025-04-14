package hcmute.lp.backend.service;

import hcmute.lp.backend.model.dto.product.ProductDto;
import hcmute.lp.backend.model.dto.product.ProductRequest;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();
    ProductDto getProductById(int id);
    ProductDto createProduct(ProductRequest productRequest);
    ProductDto updateProduct(int id, ProductRequest productRequest);
    void deleteProduct(int id);
    boolean existsById(int id);
    boolean existsByName(String name);
}
