package com.btbatux.dream_shops.service.category;

import com.btbatux.dream_shops.exception.AlreadyExistsException;
import com.btbatux.dream_shops.exception.ResourceNotFoundException;
import com.btbatux.dream_shops.model.Category;
import com.btbatux.dream_shops.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).
                orElseThrow(() ->
                        new ResourceNotFoundException("Category not found!"));

    }


    @Override
    public Category getCategoryByName(String name) {
        return Optional.ofNullable(categoryRepository.findByName(name))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + name));
    }




    @Override
    public List<Category> getAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();
        // Liste boşsa istisna fırlatılır
        if (allCategories.isEmpty()) {
            throw new ResourceNotFoundException("Category not found!");
        }
        return allCategories;  // Boş değilse liste döndürülür
    }



    @Override
    public Category addCategory(Category category) {
        // Veritabanında aynı isimde kategori var mı kontrol ediliyor
        if (categoryRepository.findByName(category.getName()) != null) {
            throw new AlreadyExistsException("Category with name " + category.getName() + " already exists.");
        }
        // Eğer aynı isimde bir kategori yoksa, yeni kategori ekleniyor
        return categoryRepository.save(category);
    }



    @Override
    public Category updateCategory(Category category, Long id) {
        // Kategori bulunamazsa, getCategoryById metodu ResourceNotFoundException fırlatır
        Category oldCategory = getCategoryById(id);
        // Güncelleme işlemi
        oldCategory.setName(category.getName());
        // Veritabanına kaydetme
        return categoryRepository.save(oldCategory);
    }



    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new ResourceNotFoundException("Category not found!");
                });
    }

}
