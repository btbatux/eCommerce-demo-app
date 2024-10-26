package com.btbatux.dream_shops.service.product;

import com.btbatux.dream_shops.dto.ImageDto;
import com.btbatux.dream_shops.dto.ProductDto;
import com.btbatux.dream_shops.exception.ProductNotFoundException;
import com.btbatux.dream_shops.model.Category;
import com.btbatux.dream_shops.model.Image;
import com.btbatux.dream_shops.model.Product;
import com.btbatux.dream_shops.repository.CategoryRepository;
import com.btbatux.dream_shops.repository.ImageRepository;
import com.btbatux.dream_shops.repository.ProductRepository;
import com.btbatux.dream_shops.request.AddProductRequest;
import com.btbatux.dream_shops.request.UpdateProductRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          ModelMapper modelMapper,
                          ImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public Product addProduct(AddProductRequest addProductRequest) {
        try {
            Category category = Optional
                    .ofNullable(categoryRepository.findByName(addProductRequest.getCategory().getName()))
                    .orElseGet(() -> {
                        Category newCategory = new Category(addProductRequest.getCategory().getName());
                        return categoryRepository.save(newCategory);
                    });
            addProductRequest.setCategory(category);
            return productRepository.save(createProduct(addProductRequest, category));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private Product createProduct(AddProductRequest addProductRequest,
                                  Category category) {

        return new Product(addProductRequest.getName(),
                addProductRequest.getBrand(),
                addProductRequest.getPrice(),
                addProductRequest.getInventory(),
                addProductRequest.getDescription(),
                category);
    }


    @Override
    public ProductDto getProductById(Long id) {
        return getConvertedProduct(productRepository.findById(id).orElseThrow(() ->
                new ProductNotFoundException("Product Not Found!")));
    }


    @Override
    public void deleteProductById(Long productId) {
        productRepository.findById(productId).ifPresentOrElse(productRepository::delete, () ->
        {
            throw new ProductNotFoundException("Product Not Found!");
        });
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long productId) {
        return productRepository.findById(productId).
                map(existingProduct -> existingUpdateProduct(existingProduct, request)).
                map(productRepository::save).
                orElseThrow(() -> new ProductNotFoundException("Product Not Found!"));

    }

    private Product existingUpdateProduct(Product product,
                                          UpdateProductRequest productUpdateRequest) {
        product.setName(productUpdateRequest.getName());
        product.setBrand(productUpdateRequest.getBrand());
        product.setPrice(productUpdateRequest.getPrice());
        product.setInventory(productUpdateRequest.getInventory());
        product.setDescription(productUpdateRequest.getDescription());

        Category category = categoryRepository.findByName(productUpdateRequest.getCategory().getName());
        product.setCategory(category);
        return product;

    }


    @Override
    public List<ProductDto> getAllProducts() {
        return getConvertedProducts(productRepository.findAll());

    }


    @Override
    public List<Product> getProductByName(String name) {
        List<Product> productList = productRepository.findByName(name);
        if (productList.isEmpty()) {
            throw new ProductNotFoundException("Product Not Found!");  // Hata fırlatılıyor
        }
        return productList;
    }


    @Override
    public List<Product> getProductsByCategory(String category) {
        List<Product> productList = productRepository.findByCategoryName(category);
        if (productList.isEmpty()) {
            throw new ProductNotFoundException("Product Not Found!");
        }
        return productList;
        //   return emptyProductValid(productList);
    }


//    private List<Product> emptyProductValid(List<Product> productList) {
//        try {
//            if (productList.isEmpty()) {
//                throw new ProductNotFoundException("Product Not Found!");
//            }
//            // Eksik veya hatalı verileri kontrol edelim
//            List<Product> validProduct = productList.stream()
//                    .filter(Objects::nonNull)  // null olan ürünleri filtrele
//                    .filter(product -> product.getName() != null &&
//                            product.getPrice() != null)  // gerekli alanlar boş mu kontrol et
//                    .toList();
//
//            if (validProduct.size() < productList.size()) {
//                throw new EmptyProductException("Some products have missing data and were filtered out.");
//            }
//            return validProduct;
//        } catch (ProductNotFoundException | EmptyProductException e) {
//            throw new RuntimeException(e);
//        }
//    }


    @Override
    public List<Product> getProductsByBrand(String brand) {

        List<Product> productList = productRepository.findByBrand(brand);
        if (productList.isEmpty()) {
            throw new ProductNotFoundException("Product Not Found!");
        }
        return productList;
    }


    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        List<Product> productList = productRepository.findByCategoryNameAndBrand(category, brand);
        if (productList.isEmpty()) {
            throw new ProductNotFoundException("Product Not Found!");
        }
        return productList;
    }


    @Override
    public List<Product> getProductsByBrandAndName(String brandName, String productName) {
        List<Product> productList = productRepository.findByBrandAndName(brandName, productName);
        if (productList.isEmpty()) {
            throw new ProductNotFoundException("Product Not Found!");
        }
        return productList;
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.totalInventoryByBrandAndName(brand, name);
    }


    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::converToDto).toList();
    }

    @Override
    public ProductDto getConvertedProduct(Product product) {
        return converToDto(product);
    }


    @Override
    public ProductDto converToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);

        List<Image> imagesList = imageRepository.findByProductId(product.getId());

        List<ImageDto> imageDtos = imagesList.stream().map(image -> modelMapper.
                        map(image, ImageDto.class)).
                toList();

        productDto.setImages(imageDtos);
        return productDto;
    }
}
