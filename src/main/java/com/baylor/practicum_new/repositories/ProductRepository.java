package com.baylor.practicum_new.repositories;

import com.baylor.practicum_new.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUser_UserId(Long userId);

    @Query(value = "select * from Product", nativeQuery = true)
    List<Product> totalNumberOfProducts();

    List<Product> findByCategoriesCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.categories IS EMPTY")
    List<Product> findProductsWithoutCategories();

    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.categoryId = :categoryId")
    List<Product> findProductsByCategoryId(@Param("categoryId") Long categoryId);

}
