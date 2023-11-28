package com.baylor.practicum_new.services;

import com.baylor.practicum_new.dto.*;
import com.baylor.practicum_new.entities.Category;
import com.baylor.practicum_new.entities.Product;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface ProductService {
    ProductDTO createProduct(Long userId, String productName, String description, Set<Long> categoryIds);
    List<ProductDTO> getAllProducts();
    List<ProductDTO> getProductsByUserId(Long userId);
    List<UserProductsDTO> getAllUsersWithProducts();

    List<Product> totalNumberOfProducts();

//    ProductCategoryDTO linkCategories(Long productId, Set<Long> categoryIds);

    public Product saveProductAndLinkToCategories(ProductCategoryDTO productCategoryDTO);

    List<Product> getProductsWithCategories(Long categoryId);

    List<ProductWithCategories> getCategoriesWithProducts();

    public void bulkCreateUsersAndProducts(List<BulkUploadDTO.UserProductInput> userInputs);
}

