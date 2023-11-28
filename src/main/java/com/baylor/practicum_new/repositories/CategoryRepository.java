package com.baylor.practicum_new.repositories;

import com.baylor.practicum_new.dto.ProductWithCategories;
import com.baylor.practicum_new.entities.Category;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

//    List<Category> findByUser_UserId(Long userId);

//    @Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.products")
    @Query(value = "SELECT \n" +
            "    p.product_id productId,\n" +
            "    p.product_name productName,\n" +
            "    p.description description,\n" +
            "    (\n" +
            "        SELECT GROUP_CONCAT(cg.category_id ORDER BY cg.category_id) \n" +
            "        FROM categories cg\n" +
            "        JOIN product_category pc ON cg.category_id = pc.category_id\n" +
            "        WHERE pc.product_id = p.product_id\n" +
            "    ) AS categoryIds\n" +
            "FROM \n" +
            "    products p\n" +
            "ORDER BY \n" +
            "    p.product_id;", nativeQuery = true)
    List<Object[]> findCategoriesWithProducts();
}
