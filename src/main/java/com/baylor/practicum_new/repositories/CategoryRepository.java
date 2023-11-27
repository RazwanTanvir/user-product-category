package com.baylor.practicum_new.repositories;

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

    @Query("SELECT DISTINCT c FROM Category c JOIN FETCH c.products")
    List<Category> findCategoriesWithProducts();





}
