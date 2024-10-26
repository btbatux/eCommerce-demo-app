package com.btbatux.dream_shops.service.product;

import com.btbatux.dream_shops.dto.ProductDto;
import com.btbatux.dream_shops.model.Product;
import com.btbatux.dream_shops.request.AddProductRequest;
import com.btbatux.dream_shops.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest addProductRequest);

    ProductDto getProductById(Long id);

    void deleteProductById(Long id);

    Product updateProduct(UpdateProductRequest product, Long productId);

    List<ProductDto> getAllProducts();

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByCategoryAndBrand(String category, String brand);

    List<Product> getProductByName(String product);

    List<Product> getProductsByBrandAndName(String category, String name);

    Long countProductsByBrandAndName(String brand, String name);

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto getConvertedProduct(Product product);

    ProductDto converToDto(Product product);
}
