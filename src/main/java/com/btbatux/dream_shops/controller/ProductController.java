package com.btbatux.dream_shops.controller;

import com.btbatux.dream_shops.dto.ProductDto;
import com.btbatux.dream_shops.exception.ProductNotFoundException;
import com.btbatux.dream_shops.exception.ResourceNotFoundException;
import com.btbatux.dream_shops.model.Product;
import com.btbatux.dream_shops.request.AddProductRequest;
import com.btbatux.dream_shops.request.UpdateProductRequest;
import com.btbatux.dream_shops.response.ApiResponse;
import com.btbatux.dream_shops.service.product.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    protected final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<ProductDto> productsDto = productService.getAllProducts();
        try {
            // Ürünler varsa başarılı yanıt döndürülür.
            return ResponseEntity
                    .ok(new ApiResponse("Success", productsDto));
        } catch (Exception e) {
            // Genel hatalar için 500 Internal Server Error döndürülür.
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("An error occurred: ", e.getMessage()));
        }
    }


    @GetMapping("/{productId}/product")
    public ResponseEntity<ApiResponse>getProductById(@PathVariable Long productId) {
        try {
            return ResponseEntity
                    .ok(new ApiResponse("Product Found",
                            productService.getProductById(productId)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));

        }
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest productRequest) {
        try {
            Product product = productService.addProduct(productRequest);
            return ResponseEntity
                    .ok(new ApiResponse("Product added!", product));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }


    @PutMapping("/product/{id}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest productRequest,
                                                     @PathVariable Long id) {
        try {
            Product product = productService.updateProduct(productRequest, id);
            return ResponseEntity
                    .ok(new ApiResponse("Product updated!", product));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }

    }

    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity
                    .ok((new ApiResponse("Product Deleted! ", productId)));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }


    @GetMapping("/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brandName,
                                                                @RequestParam String productName) {
        try {
            List<Product> productList =
                    productService.getProductsByBrandAndName(brandName, productName);
            return ResponseEntity
                    .ok(new ApiResponse("Success", productList));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("", productName));
        }
    }


    @GetMapping("/products/by-brand")
    public ResponseEntity<ApiResponse> getProductByBrand(@RequestParam String brandName) {
        try {
            List<Product> productList = productService.getProductsByBrand(brandName);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(productList);

            if (productList.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND)
                        .body(new ApiResponse("No Products", null));
            }
            return ResponseEntity
                    .ok(new ApiResponse("Success", convertedProducts));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .ok(new ApiResponse(e.getMessage(), brandName));
        }
    }

    @GetMapping("/products/{name}/products")
    public ResponseEntity<ApiResponse> getProductsByName(@PathVariable String name) {
        try {
            List<Product> productList = productService.getProductByName(name);
            List<ProductDto> productDtoList = productService.getConvertedProducts(productList);


            if (productList.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND)
                        .body(new ApiResponse("No Products", null));
            }
            return ResponseEntity
                    .ok(new ApiResponse("Success", productDtoList));

        } catch (ProductNotFoundException e) {  // ProductNotFoundException yakalanıyor
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));  // 404 ile dönüyor
        } catch (Exception e) {  // Diğer beklenmedik hatalar için
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Internal Server Error", null));
        }
    }


    @GetMapping("/products/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@RequestParam String categoryName,
                                                                    @RequestParam String brandName) {
        List<Product> productList =
                productService.getProductsByCategoryAndBrand(categoryName, brandName);
        try {
            if (productList.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).
                        body(new ApiResponse("No Products", productList));
            }
            return ResponseEntity
                    .ok(new ApiResponse("Success", productList));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }


    @GetMapping("/product/{category}/all/products")
    public ResponseEntity<ApiResponse> getAllProductsByCategory(@PathVariable String category) {
        try {
            List<Product> productList =
                    productService.getProductsByCategory(category);
            List<ProductDto> productDtoList = productService.getConvertedProducts(productList);


            return ResponseEntity.
                    ok(new ApiResponse("Success", productDtoList));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).
                    body(new ApiResponse(e.getMessage(), null));
        }
    }


    @GetMapping("/product/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brandName,
                                                                   @RequestParam String productName) {
        try {
            Long totalInventory = productService.countProductsByBrandAndName(brandName, productName);
            return ResponseEntity
                    .ok(new ApiResponse("Success", totalInventory));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("An error occurred: " + e.getMessage(), null));
        }
    }
}
