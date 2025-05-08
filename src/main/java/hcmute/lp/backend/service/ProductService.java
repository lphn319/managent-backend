package hcmute.lp.backend.service;

import hcmute.lp.backend.model.dto.product.ProductDto;
import hcmute.lp.backend.model.dto.product.ProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();

    Page<ProductDto> getProductsPaginated(int page, int size, String sortBy, String sortDirection);

    ProductDto getProductById(long id);

    ProductDto createProduct(ProductRequest productRequest);
    ProductDto updateProduct(long id, ProductRequest productRequest);
    void deleteProduct(long id);

}
