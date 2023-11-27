package com.baylor.practicum_new.dto;

import com.baylor.practicum_new.entities.Category;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class ProductDTO {

    private Long productId;

    private String productName;

    private String description;

    private Set<Category> categories;

}
