package hcmute.lp.backend.controller;

import hcmute.lp.backend.model.common.ApiResponse;
import hcmute.lp.backend.model.dto.product.ProductDto;
import hcmute.lp.backend.model.dto.product.ProductRequest;
import hcmute.lp.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Product API")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<ProductDto>>> getProductsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        Page<ProductDto> productsPage = productService.getProductsPaginated(page, size, sortBy, sortDirection);
    return ResponseEntity.ok(ApiResponse.success("Products retrieved successfully", productsPage));
    }

     //GET ALL PRODUCTS
     @GetMapping
     public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
         List<ProductDto> products = productService.getAllProducts();
         return ResponseEntity.ok(ApiResponse.success("Products retrieved successfully", products));
     }

     //GET PRODUCT BY ID
     @GetMapping("/{id}")
     public ResponseEntity<ApiResponse<ProductDto>> getProductById(@PathVariable int id) {
         ProductDto product = productService.getProductById(id);
         return ResponseEntity.ok(ApiResponse.success("Product found", product));
     }

     //CREATE PRODUCT
     @PostMapping
     public ResponseEntity<ApiResponse<ProductDto>> createProduct(@RequestBody ProductRequest productRequest) {
         ProductDto createdProduct = productService.createProduct(productRequest);
         return ResponseEntity.status(201).body(ApiResponse.success("Product created", createdProduct));
     }

     //UPDATE PRODUCT
     @PutMapping("/{id}")
     public ResponseEntity<ApiResponse<ProductDto>> updateProduct(@PathVariable int id, @RequestBody ProductRequest productRequest) {
         ProductDto updatedProduct = productService.updateProduct(id, productRequest);
         return ResponseEntity.ok(ApiResponse.success("Product updated successfully", updatedProduct));
     }

     //DELETE PRODUCT
     @DeleteMapping("/{id}")
     public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable int id) {
         productService.deleteProduct(id);
         return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
     }


}
