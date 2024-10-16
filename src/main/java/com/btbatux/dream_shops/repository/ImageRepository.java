package com.btbatux.dream_shops.repository;

import com.btbatux.dream_shops.model.Image;
import com.btbatux.dream_shops.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByProductId(long id);
}