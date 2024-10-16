package com.btbatux.dream_shops.repository;

import com.btbatux.dream_shops.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryName(String category);

    List<Product> findByBrand(String brand);

    List<Product> findByCategoryNameAndBrand(String category, String brand);

    List<Product> findByName(String name);

    List<Product> findByBrandAndName(String brandName, String productName);

    //Long countByBrandAndName(String brand, String name);
    @Query("SELECT SUM(p.inventory) FROM Product p WHERE p.brand = :brand AND p.name = :name")
    Long totalInventoryByBrandAndName(String brand, String name);

}