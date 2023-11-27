package com.baylor.practicum_new.dto;

import com.baylor.practicum_new.entities.Category;
import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class ProductCategoryDTO {

    private Long productId;

    private Set<Long> categoryIds;
}
