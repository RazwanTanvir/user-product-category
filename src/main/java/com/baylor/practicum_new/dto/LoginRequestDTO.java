package com.baylor.practicum_new.dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class LoginRequestDTO {

    private String email;

    private String password;
}
