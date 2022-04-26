package com.example.shoppingcart.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequestDto {

    private String email;
    private String password;


}
