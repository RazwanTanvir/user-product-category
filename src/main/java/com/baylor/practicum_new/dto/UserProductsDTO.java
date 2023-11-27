package com.baylor.practicum_new.dto;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class UserProductsDTO {

    private Long userId;

    private String userName;

    private List<ProductDTO> products;
}
