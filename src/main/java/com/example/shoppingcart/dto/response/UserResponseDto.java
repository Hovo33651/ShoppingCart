package com.example.shoppingcart.dto.response;

import com.example.shoppingcart.entity.UserType;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    private String name;
    private String surname;
    private String email;
    private UserType type;
    private String token;

}
