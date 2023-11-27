package com.baylor.practicum_new.controller;

import com.baylor.practicum_new.dto.*;
import com.baylor.practicum_new.entities.Category;
import com.baylor.practicum_new.entities.Product;
import com.baylor.practicum_new.services.CategoryService;
import com.baylor.practicum_new.services.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import java.util.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    private final ObjectMapper objectMapper;

    @Autowired
    public ProductController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody Map<String, Object> productDetails) {
        Long userId = Long.parseLong(productDetails.get("userId").toString());
        String productName = productDetails.get("productName").toString();
        String description = productDetails.get("description").toString();
        List<Long> categoryIdList = objectMapper.convertValue(productDetails.get("categoryIds"), new TypeReference<List<Long>>() {});
        Set<Long> categoryIds = new HashSet<>(categoryIdList);


        ProductDTO product = productService.createProduct(userId, productName, description, categoryIds);

        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProductDTO>> getProductsByUserId(@PathVariable Long userId) {
        List<ProductDTO> products = productService.getProductsByUserId(userId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserProductsDTO>> getProductsGroupedByUsers() {
        return new ResponseEntity<>(productService.getAllUsersWithProducts(), HttpStatus.OK);
    }

    @GetMapping("/totalProduct")
    public ResponseEntity<List<Product>> getTotalNumberOfProducts() {
        return new ResponseEntity<>(productService.totalNumberOfProducts(), HttpStatus.OK);
//        return productService.totalNumberOfProducts();
    }

    @Transactional
    @PostMapping("/link-categories")
    public ResponseEntity<Product> linkProductToCategories(
            @RequestBody ProductCategoryDTO productCategoryDTO
    ) {
        Product savedProduct = productService.saveProductAndLinkToCategories(productCategoryDTO);
        return ResponseEntity.ok(savedProduct);
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<List<Product>> listProductsByCategory(@PathVariable Long categoryId) {
        List<Product> productsWithCategories = productService.getProductsWithCategories(categoryId);
        return ResponseEntity.ok(productsWithCategories);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> listProductsGroupedByCategories() {
        List<Category> categoriesWithProducts = productService.getCategoriesWithProducts();
        return ResponseEntity.ok(categoriesWithProducts);
    }

    @PostMapping("/bulk-create")
    public ResponseEntity<?> bulkCreateProducts(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
            List<BulkUploadDTO.UserProductInput> userProductInputs = parseJsonFile(file);
            productService.bulkCreateUsersAndProducts(userProductInputs);
            return ResponseEntity.ok("Bulk data processed successfully");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error processing file: " + e.getMessage());
        }
    }

    private List<BulkUploadDTO.UserProductInput> parseJsonFile(MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, BulkUploadDTO.UserProductInput.class);
        return objectMapper.readValue(file.getInputStream(), type);
    }


}
