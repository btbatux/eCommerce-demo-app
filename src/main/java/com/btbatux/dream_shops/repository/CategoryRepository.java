package com.btbatux.dream_shops.repository;

import com.btbatux.dream_shops.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {


    Category findByName(String name);
}