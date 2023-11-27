package com.baylor.practicum_new.services.impl;

import com.baylor.practicum_new.dto.*;
import com.baylor.practicum_new.entities.Category;
import com.baylor.practicum_new.entities.Product;
import com.baylor.practicum_new.entities.User;
import com.baylor.practicum_new.repositories.CategoryRepository;
import com.baylor.practicum_new.repositories.ProductRepository;
import com.baylor.practicum_new.repositories.UserRepository;
import com.baylor.practicum_new.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ProductDTO createProduct(Long userId, String productName, String description, Set<Long> categoryIds) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));

        Product product = new Product();
        product.setUser(user);
        product.setProductName(productName);
        product.setDescription(description);

        Product savedProduct = productRepository.save(product);
        return new ProductDTO(savedProduct.getProductId(), savedProduct.getProductName(), savedProduct.getDescription(), savedProduct.getCategories());
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(product ->
                        new ProductDTO(product.getProductId(), product.getProductName(), product.getDescription(), product.getCategories())).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductsByUserId(Long userId) {
        List<Product> products = productRepository.findByUser_UserId(userId);

        return products.stream().map(product -> {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductId(product.getProductId());
            productDTO.setProductName(product.getProductName());
            productDTO.setDescription(product.getDescription());
            return productDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserProductsDTO> getAllUsersWithProducts() {
        List<User> users = userRepository.findAll();

        return users.stream().map(user -> {
            List<Product> userProducts = productRepository.findByUser_UserId(user.getUserId());
            List<ProductDTO> productDTOs = userProducts.stream()
                    .map(product -> new ProductDTO(
                            product.getProductId(),
                            product.getProductName(),
                            product.getDescription(),
                            product.getCategories()))
                    .collect(Collectors.toList());
            return new UserProductsDTO(user.getUserId(), user.getName(), productDTOs);
        }).collect(Collectors.toList());
    }

    @Override
    public List<Product> totalNumberOfProducts() {
        return productRepository.totalNumberOfProducts();
    }

    @Override
    public Product saveProductAndLinkToCategories(ProductCategoryDTO productCategoryDTO) {
        Product product = productRepository.findById(productCategoryDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productCategoryDTO.getProductId()));

        Set<Category> categories = new HashSet<>();
        for (Long categoryId : productCategoryDTO.getCategoryIds()) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));
            categories.add(category);
        }

        product.setCategories(categories);
        return productRepository.save(product);
    }

    @Override
    public List<Product> getProductsWithCategories(Long categoryId) {
        if (categoryId == 0) {
            return productRepository.findByCategoriesCategoryId(null);
        } else {
            return productRepository.findByCategoriesCategoryId(categoryId);
        }
    }

    @Override
    public List<Category> getCategoriesWithProducts() {
        return categoryRepository.findCategoriesWithProducts();
    }

    @Transactional
    public void bulkCreateUsersAndProducts(List<BulkUploadDTO.UserProductInput> userInputs) {
        for (BulkUploadDTO.UserProductInput userInput : userInputs) {
            User user = userRepository.findById(userInput.getUserId())
                    .orElseGet(() -> {
                        User newUser = new User();
                        newUser.setName(userInput.getUserName());
                        newUser.setEmail(generatePlaceholderEmail(userInput.getUserName()));
                        return userRepository.save(newUser);
                    });

            for (BulkUploadDTO.ProductInput productInput : userInput.getProducts()) {
                Product product = new Product();
                product.setProductName(productInput.getProductName());
                product.setDescription(productInput.getDescription());
                product.setUser(user);

                Set<Category> categories = new HashSet<>();
                for (Long categoryId : productInput.getCategoryIds()) {
                    Category category = categoryRepository.findById(categoryId)
                            .orElseGet(() -> {
                                Category newCategory = new Category();
                                newCategory.setCategoryId(categoryId);
                                return categoryRepository.save(newCategory);
                            });
                    categories.add(category);
                }
                product.setCategories(categories);

                productRepository.save(product);
            }


        }
    }

    private String generatePlaceholderEmail (String userName){
        return userName.replaceAll(" ", "_").toLowerCase() + "@placeholder.com";
    }
}

