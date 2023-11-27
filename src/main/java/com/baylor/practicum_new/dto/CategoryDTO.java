package com.baylor.practicum_new.dto;

import com.baylor.practicum_new.entities.Product;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class CategoryDTO {

    private Long categoryId;

    private String name;

    private String description;

    private Set<Product> products;
}
