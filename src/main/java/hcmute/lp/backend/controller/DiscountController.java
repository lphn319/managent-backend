package hcmute.lp.backend.controller;

import hcmute.lp.backend.model.common.ApiResponse;
import hcmute.lp.backend.model.dto.discount.DiscountDto;
import hcmute.lp.backend.model.dto.discount.DiscountRequest;
import hcmute.lp.backend.service.DiscountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/discounts")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Discount API")
public class DiscountController {
    @Autowired
    private DiscountService discountService;

    //GET ALL DISCOUNTS
     @GetMapping
     public ResponseEntity<ApiResponse<List<DiscountDto>>> getAllDiscounts() {
         List<DiscountDto> discounts = discountService.getAllDiscounts();
         return ResponseEntity.ok(ApiResponse.success("Discounts retrieved successfully", discounts));
     }

    //GET DISCOUNT BY ID
     @GetMapping("/{id}")
     public ResponseEntity<ApiResponse<DiscountDto>> getDiscountById(@PathVariable long id) {
         DiscountDto discount = discountService.getDiscountById(id);
         return ResponseEntity.ok(ApiResponse.success("Discount found", discount));
     }

    //GET DISCOUNT BY CODE
     @GetMapping("/code/{code}")
     public ResponseEntity<ApiResponse<DiscountDto>> getDiscountByCode(@PathVariable String code) {
         DiscountDto discount = discountService.getDiscountByCode(code);
         return ResponseEntity.ok(ApiResponse.success("Discount found", discount));
     }

    //CREATE DISCOUNT
     @PostMapping
     public ResponseEntity<ApiResponse<DiscountDto>> createDiscount(@RequestBody DiscountRequest discountRequest) {
         DiscountDto createdDiscount = discountService.createDiscount(discountRequest);
         return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Discount created", createdDiscount));
     }

    //UPDATE DISCOUNT
     @PutMapping("/{id}")
     public ResponseEntity<ApiResponse<DiscountDto>> updateDiscount(@PathVariable long id, @RequestBody DiscountRequest discountRequest) {
         DiscountDto updatedDiscount = discountService.updateDiscount(id, discountRequest);
         return ResponseEntity.ok(ApiResponse.success("Discount updated successfully", updatedDiscount));
     }

    //DELETE DISCOUNT
     @DeleteMapping("/{id}")
     public ResponseEntity<ApiResponse<Void>> deleteDiscount(@PathVariable long id) {
         discountService.deleteDiscount(id);
         return ResponseEntity.ok(ApiResponse.success("Discount deleted successfully", null));
     }

    @GetMapping
    @RequestMapping("is-active/{id}")
    public ResponseEntity<ApiResponse<Boolean>> isDiscountActive(@PathVariable long id) {
        boolean isActive = discountService.isDiscountActive(id);
        return ResponseEntity.ok(ApiResponse.success("Discount active status retrieved successfully", isActive));
    }
}
